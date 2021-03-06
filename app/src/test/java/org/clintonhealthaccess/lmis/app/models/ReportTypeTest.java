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

package org.clintonhealthaccess.lmis.app.models;

import org.clintonhealthaccess.lmis.app.activities.reports.FacilityConsumptionReportRH1Activity;
import org.clintonhealthaccess.lmis.utils.RobolectricGradleTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
public class ReportTypeTest {

    @Test
    public void shouldReturnTwoReportTypesForCategoryVaccines() throws Exception {
        List<ReportType> reportTypesForCategory = ReportType.getReportTypesForCategory("Vaccine");
        assertThat(reportTypesForCategory.size(), is(2));
        assertThat(reportTypesForCategory, containsInAnyOrder(ReportType.MonthlyHealthFacilityDevicesUtilizationReport, ReportType.MonthlyHealthFacilityVaccinesUtilizationReport));
    }

    @Test
    public void shouldReturnTwoReportsForCategoryMalaria() throws Exception {
        List<ReportType> reportTypesForCategory = ReportType.getReportTypesForCategory("Malaria category");
        assertThat(reportTypesForCategory.size(), is(2));
        assertThat(reportTypesForCategory, containsInAnyOrder(ReportType.FacilityStockReport, ReportType.FacilityConsumptionReportRH1));
    }

    @Test
    public void shouldReturnTwoReportsForCategoryMarternal() throws Exception {
        List<ReportType> reportTypesForCategory = ReportType.getReportTypesForCategory("Marternal category");
        assertThat(reportTypesForCategory.size(), is(2));
        assertThat(reportTypesForCategory, containsInAnyOrder(ReportType.FacilityStockReport, ReportType.FacilityConsumptionReportRH1));
    }

    @Test
    public void shouldReturnTwoReportsForCategoryFamilyPlaning() throws Exception {
        List<ReportType> reportTypesForCategory = ReportType.getReportTypesForCategory("Family Planning");

        assertThat(reportTypesForCategory.size(), is(3));
        assertThat(reportTypesForCategory, containsInAnyOrder(ReportType.FacilityConsumptionReportRH2, ReportType.FacilityStockReport, ReportType.FacilityConsumptionReportRH1));
    }

    @Test
    public void shouldReturnTwoReportsForCategoryAntibiotics() throws Exception {
        List<ReportType> reportTypesForCategory = ReportType.getReportTypesForCategory("Antibiotics");
        assertThat(reportTypesForCategory.size(), is(2));
        assertThat(reportTypesForCategory, containsInAnyOrder(ReportType.FacilityStockReport, ReportType.FacilityConsumptionReportRH1));
    }

    @Test
    public void shouldGotToFacilityConsumptionReportRH1ActivityForRH1report() throws Exception {
        assertThat(ReportType.FacilityConsumptionReportRH1.getReportActivity().getName(), is(FacilityConsumptionReportRH1Activity.class.getName()));
    }
}