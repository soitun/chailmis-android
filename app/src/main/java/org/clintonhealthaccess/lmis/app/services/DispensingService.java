/*
 * Copyright (c) 2014, Thoughtworks Inc
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package org.clintonhealthaccess.lmis.app.services;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import org.clintonhealthaccess.lmis.app.LmisException;
import org.clintonhealthaccess.lmis.app.models.Commodity;
import org.clintonhealthaccess.lmis.app.models.Dispensing;
import org.clintonhealthaccess.lmis.app.models.DispensingItem;
import org.clintonhealthaccess.lmis.app.models.Receive;
import org.clintonhealthaccess.lmis.app.models.ReceiveItem;
import org.clintonhealthaccess.lmis.app.models.reports.UtilizationValue;
import org.clintonhealthaccess.lmis.app.persistence.DbUtil;
import org.clintonhealthaccess.lmis.app.persistence.LmisSqliteOpenHelper;
import org.clintonhealthaccess.lmis.app.utils.DateUtil;
import org.clintonhealthaccess.lmis.app.utils.Helpers;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.j256.ormlite.android.apptools.OpenHelperManager.getHelper;
import static com.j256.ormlite.android.apptools.OpenHelperManager.releaseHelper;
import static com.j256.ormlite.dao.DaoManager.createDao;
import static org.clintonhealthaccess.lmis.app.persistence.DbUtil.initialiseDao;

public class DispensingService {

    @Inject
    StockService stockService;
    @Inject
    CommoditySnapshotService commoditySnapshotService;
    @Inject
    Context context;
    @Inject
    private DbUtil dbUtil;

    public void addDispensing(final Dispensing dispensing) {
        dbUtil.withDaoAsBatch(DispensingItem.class, new DbUtil.Operation<DispensingItem, DispensingItem>() {
            @Override
            public DispensingItem operate(Dao<DispensingItem, String> dao) throws SQLException {
                saveDispensing(dispensing);
                for (DispensingItem item : dispensing.getDispensingItems()) {
                    item.setDispensing(dispensing);
                    dao.create(item);
                    adjustStockLevel(item);
                    commoditySnapshotService.add(item);
                }
                return null;
            }
        });
    }


    private void saveDispensing(final Dispensing dispensing) throws SQLException {
        GenericDao<Dispensing> dispensingDao = new GenericDao<>(Dispensing.class, context);
        dispensingDao.create(dispensing);
    }

    private void adjustStockLevel(DispensingItem dispensing) throws SQLException {
        stockService.reduceStockLevelFor(dispensing.getCommodity(), dispensing.getQuantity(), dispensing.created());
    }

    public String getNextPrescriptionId() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        String currentMonth = simpleDateFormat.format(new Date());
        int numberOfDispensingsToPatientsThisMonth = getDispensingsToPatientsThisMonth();
        return getFormattedPrescriptionId(currentMonth, numberOfDispensingsToPatientsThisMonth);
    }

    private String getFormattedPrescriptionId(String currentMonth, int numberOfDispensingsToPatientsThisMonth) {
        String stringOfZeros = "";

        int length = String.valueOf(numberOfDispensingsToPatientsThisMonth).length();
        if (length < 4) {
            for (int i = 0; i < 4 - length; i++) {
                stringOfZeros += "0";
            }
        }
        return String.format("%s%d-%s", stringOfZeros, numberOfDispensingsToPatientsThisMonth + 1, currentMonth);
    }

    private int getDispensingsToPatientsThisMonth() {
        return dbUtil.withDao(Dispensing.class, new DbUtil.Operation<Dispensing, Integer>() {
            @Override
            public Integer operate(Dao<Dispensing, String> dao) throws SQLException {
                QueryBuilder<Dispensing, String> dispensingStringQueryBuilder = dao.queryBuilder();
                Date firstDay = Helpers.firstDayOfMonth(new Date());
                Date lastDay = Helpers.lastDayOfMonth(new Date());
                dispensingStringQueryBuilder.where().between("created", firstDay, lastDay);
                PreparedQuery<Dispensing> query = dispensingStringQueryBuilder.prepare();
                List<Dispensing> dispensingList = dao.query(query);
                return dispensingList.size();
            }


        });
    }

    public List<UtilizationValue> getDispensedValues(Commodity commodity, Date startDate, Date endDate, boolean forVial) {
        List<DispensingItem> items = GenericService.getItems(commodity, startDate, endDate, Dispensing.class, DispensingItem.class, context);

        List<UtilizationValue> utilizationValues = new ArrayList<>();

        Calendar calendar = DateUtil.calendarDate(startDate);

        Date upperLimitDate = DateUtil.addDayOfMonth(endDate, 1);

        int dosesPerVial = commodity.dosesPerVial();

        while (calendar.getTime().before(upperLimitDate)) {
            int dayDispensingItems = getTotal(calendar.getTime(), items);

            if (forVial) {
                dayDispensingItems = dayDispensingItems * dosesPerVial;
            }

            UtilizationValue utilizationValue =
                    new UtilizationValue(DateUtil.dayNumber(calendar.getTime()), dayDispensingItems);
            utilizationValues.add(utilizationValue);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return utilizationValues;
    }

    private int getTotal(final Date date, List<DispensingItem> dispensingItems) {
        List<DispensingItem> daysDispensingItems = FluentIterable.from(dispensingItems).filter(new Predicate<DispensingItem>() {
            @Override
            public boolean apply(DispensingItem input) {
                return DateUtil.equal(input.created(), date);
            }
        }).toList();

        int total = 0;
        for (DispensingItem dispensingItem : daysDispensingItems) {
            total += dispensingItem.getQuantity();
        }

        return total;
    }
}
