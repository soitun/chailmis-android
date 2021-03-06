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

package org.clintonhealthaccess.lmis.app.persistence.migrations;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

import org.clintonhealthaccess.lmis.app.LmisException;
import org.clintonhealthaccess.lmis.app.models.Adjustment;
import org.clintonhealthaccess.lmis.app.models.Allocation;
import org.clintonhealthaccess.lmis.app.models.AllocationItem;
import org.clintonhealthaccess.lmis.app.models.Category;
import org.clintonhealthaccess.lmis.app.models.Commodity;
import org.clintonhealthaccess.lmis.app.models.CommodityAction;
import org.clintonhealthaccess.lmis.app.models.CommodityActionDataSet;
import org.clintonhealthaccess.lmis.app.models.CommodityActionValue;
import org.clintonhealthaccess.lmis.app.models.CommoditySnapshot;
import org.clintonhealthaccess.lmis.app.models.DataSet;
import org.clintonhealthaccess.lmis.app.models.Dispensing;
import org.clintonhealthaccess.lmis.app.models.DispensingItem;
import org.clintonhealthaccess.lmis.app.models.Loss;
import org.clintonhealthaccess.lmis.app.models.LossItem;
import org.clintonhealthaccess.lmis.app.models.LossItemDetail;
import org.clintonhealthaccess.lmis.app.models.Order;
import org.clintonhealthaccess.lmis.app.models.OrderItem;
import org.clintonhealthaccess.lmis.app.models.OrderReason;
import org.clintonhealthaccess.lmis.app.models.OrderType;
import org.clintonhealthaccess.lmis.app.models.Receive;
import org.clintonhealthaccess.lmis.app.models.ReceiveItem;
import org.clintonhealthaccess.lmis.app.models.StockItem;
import org.clintonhealthaccess.lmis.app.models.StockItemSnapshot;
import org.clintonhealthaccess.lmis.app.models.User;
import org.clintonhealthaccess.lmis.app.models.alerts.AllocationAlert;
import org.clintonhealthaccess.lmis.app.models.alerts.LowStockAlert;
import org.clintonhealthaccess.lmis.app.models.alerts.MonthlyStockCountAlert;
import org.clintonhealthaccess.lmis.app.models.alerts.RoutineOrderAlert;
import org.clintonhealthaccess.lmis.app.persistence.Migration;

import java.sql.SQLException;

import static com.j256.ormlite.table.TableUtils.createTableIfNotExists;

public class CreateInitTables implements Migration {
    @Override
    public void up(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            createTableIfNotExists(connectionSource, Category.class);
            createTableIfNotExists(connectionSource, Commodity.class);
            createTableIfNotExists(connectionSource, User.class);
            createTableIfNotExists(connectionSource, Dispensing.class);
            createTableIfNotExists(connectionSource, DispensingItem.class);
            createTableIfNotExists(connectionSource, StockItem.class);
            createTableIfNotExists(connectionSource, OrderReason.class);
            createTableIfNotExists(connectionSource, OrderItem.class);
            createTableIfNotExists(connectionSource, Order.class);
            createTableIfNotExists(connectionSource, CommoditySnapshot.class);
            createTableIfNotExists(connectionSource, DataSet.class);
            createTableIfNotExists(connectionSource, Loss.class);
            createTableIfNotExists(connectionSource, LossItem.class);
            createTableIfNotExists(connectionSource, LossItemDetail.class);
            createTableIfNotExists(connectionSource, Allocation.class);
            createTableIfNotExists(connectionSource, AllocationItem.class);
            createTableIfNotExists(connectionSource, Receive.class);
            createTableIfNotExists(connectionSource, ReceiveItem.class);
            createTableIfNotExists(connectionSource, CommodityAction.class);
            createTableIfNotExists(connectionSource, OrderType.class);
            createTableIfNotExists(connectionSource, CommodityActionValue.class);
            createTableIfNotExists(connectionSource, LowStockAlert.class);
            createTableIfNotExists(connectionSource, RoutineOrderAlert.class);
            createTableIfNotExists(connectionSource, AllocationAlert.class);
            createTableIfNotExists(connectionSource, Adjustment.class);
            createTableIfNotExists(connectionSource, MonthlyStockCountAlert.class);
            createTableIfNotExists(connectionSource, StockItemSnapshot.class);
            createTableIfNotExists(connectionSource, CommodityActionDataSet.class);
        } catch (SQLException e) {
            throw new LmisException(e);
        }
    }

    @Override
    public void down(SQLiteDatabase db, ConnectionSource connectionSource) {

    }
}
