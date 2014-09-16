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

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LossItemTest {
    @Test
    public void shouldCreateActivitiesForMissingWastedAndExpiries() throws Exception {
        Commodity commodity = mock(Commodity.class);
        CommodityActivity wasted = new CommodityActivity(commodity, "12", "12", "waste");
        CommodityActivity missing = new CommodityActivity(commodity, "12", "12", "missing");
        CommodityActivity expiries = new CommodityActivity(commodity, "12", "12", "expired");
        when(commodity.getCommodityActivitiesSaved()).thenReturn(new ArrayList<CommodityActivity>(Arrays.asList(wasted, missing, expiries)));
        LossItem lossItem = new LossItem(commodity, 10);
        lossItem.setWastages(10);
        lossItem.setExpiries(20);
        lossItem.setMissing(30);
        assertThat(lossItem.getActivitiesValues().size(), is(3));
        CommodityActivityValue wastedValue = new CommodityActivityValue(wasted, 10);
        CommodityActivityValue expiriesValue = new CommodityActivityValue(expiries, 20);
        CommodityActivityValue missingValue = new CommodityActivityValue(missing, 30);
        assertThat(lossItem.getActivitiesValues(), containsInAnyOrder(wastedValue, expiriesValue, missingValue));
    }
}