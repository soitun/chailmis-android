/*
 * Copyright (c) 2014, Clinton Health Access Initiative
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

package org.clintonhealthaccess.lmis.app.fragments;

import android.app.Dialog;
import android.widget.Button;
import android.widget.ListView;

import com.google.inject.AbstractModule;

import org.clintonhealthaccess.lmis.app.R;
import org.clintonhealthaccess.lmis.app.models.Dispensing;
import org.clintonhealthaccess.lmis.app.services.DispensingService;
import org.clintonhealthaccess.lmis.utils.RobolectricGradleTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowDialog;

import static junit.framework.Assert.assertFalse;
import static org.clintonhealthaccess.lmis.utils.TestInjectionUtil.setUpInjection;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.robolectric.util.FragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
public class DispenseConfirmationFragmentTest {

    private DispenseConfirmationFragment dispenseConfirmationFragment;

    private DispensingService mockDispensingService;


    @Before
    public void setUp() throws Exception {

        mockDispensingService = mock(DispensingService.class);


        setUpInjection(this, new AbstractModule() {
            @Override
            protected void configure() {
                bind(DispensingService.class).toInstance(mockDispensingService);
            }
        });

        dispenseConfirmationFragment = DispenseConfirmationFragment.newInstance(new Dispensing());
        startFragment(dispenseConfirmationFragment);
    }

    @Test
    public void testConfirmButtonExists() throws Exception {
        Dialog dialog = ShadowDialog.getLatestDialog();
        Button buttonClose = (Button) dialog.findViewById(R.id.buttonDispenseConfirm);
        assertThat(buttonClose, not(nullValue()));
        assertThat(buttonClose.getText().toString(), is(Robolectric.application.getString(R.string.confirm)));
    }

    @Test
    public void testConfirmButtonHasDifferentTextIfDispensingToFacility() throws Exception {
        Dispensing dispensing = new Dispensing();
        dispensing.setDispenseToFacility(true);
        dispenseConfirmationFragment = DispenseConfirmationFragment.newInstance(dispensing);
        startFragment(dispenseConfirmationFragment);
        Dialog dialog = ShadowDialog.getLatestDialog();
        Button buttonClose = (Button) dialog.findViewById(R.id.buttonDispenseConfirm);
        assertThat(buttonClose, not(nullValue()));
        assertThat(buttonClose.getText().toString(), is(Robolectric.application.getString(R.string.confirm_facility)));
    }

    @Test
    public void testListViewExists() throws Exception {
        Dialog dialog = ShadowDialog.getLatestDialog();
        ListView listView = (ListView) dialog.findViewById(R.id.listViewConfirmItems);
        assertThat(listView, not(nullValue()));
        assertThat(listView.getAdapter(), not(nullValue()));

    }


    @Test
    public void testGoBackButtonExists() throws Exception {
        Dialog dialog = ShadowDialog.getLatestDialog();
        Button buttonGoBack = (Button) dialog.findViewById(R.id.buttonDispenseGoBack);
        assertThat(buttonGoBack, not(nullValue()));

    }

    @Test
    public void testConfirmButtonLogic() throws Exception {
        Dialog dialog = ShadowDialog.getLatestDialog();
        Button buttonClose = (Button) dialog.findViewById(R.id.buttonDispenseConfirm);
        assertThat(dispenseConfirmationFragment.dispensing, not(nullValue()));

        buttonClose.callOnClick();
        verify(mockDispensingService).addDispensing(Matchers.<Dispensing>anyObject());
        assertFalse(dialog.isShowing());

    }

    @Test
    public void testGoBackButtonLogic() throws Exception {
        Dialog dialog = ShadowDialog.getLatestDialog();
        Button buttonGoBack = (Button) dialog.findViewById(R.id.buttonDispenseGoBack);
        assertThat(dispenseConfirmationFragment.dispensing, not(nullValue()));
        buttonGoBack.callOnClick();
        verify(mockDispensingService, never()).addDispensing(Matchers.<Dispensing>anyObject());
        assertFalse(dialog.isShowing());

    }
}
