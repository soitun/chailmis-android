package org.clintonhealthaccess.lmis.app.models.api;

import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SmsDataValueSetTest {
    @Test
    public void shouldConvertDataValueSetToSmsValueSet() throws Exception {
        DataValue dataValue1 = DataValue.builder().dataElement("element_1").value("11").build();
        DataValue dataValue2 = DataValue.builder().dataElement("element_2").value("22").build();
        DataValueSet dataValueSet = new DataValueSet();
        dataValueSet.setDataSet("set_1");
        dataValueSet.setDataValues(newArrayList(dataValue1, dataValue2));

        SmsValueSet smsValueSet = dataValueSet.toSmsValueSet();
        assertThat(smsValueSet.toString(), is("set_1 element_1.11.element_2.22"));
    }
}