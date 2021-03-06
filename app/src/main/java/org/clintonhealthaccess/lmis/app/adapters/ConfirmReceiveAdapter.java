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

package org.clintonhealthaccess.lmis.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.clintonhealthaccess.lmis.app.R;
import org.clintonhealthaccess.lmis.app.models.ReceiveItem;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ConfirmReceiveAdapter extends ArrayAdapter<ReceiveItem> {

    private int resource;
    private boolean quantityAllocatedDisplay = true;

    public ConfirmReceiveAdapter(Context context, int resource, List<ReceiveItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, parent, false);

        holder.textViewCommodityName = (TextView) convertView.findViewById(R.id.textViewCommodityName);
        holder.textViewQuantityAllocated = (TextView) convertView.findViewById(R.id.textViewQuantityAllocated);
        holder.textViewQuantityReceived = (TextView) convertView.findViewById(R.id.textViewQuantityReceived);
        holder.textViewQuantityDifference = (TextView) convertView.findViewById(R.id.textViewQuantityDifference);

        holder.textViewCommodityName.setText(getItem(position).getCommodity().getName());
        holder.textViewQuantityAllocated.setText(String.valueOf(getItem(position).getQuantityAllocated()));
        holder.textViewQuantityReceived.setText(String.valueOf(getItem(position).getQuantityReceived()));
        holder.textViewQuantityDifference.setText(String.valueOf(getItem(position).getDifference()));

        if(!quantityAllocatedDisplay){
            holder.textViewQuantityAllocated.setVisibility(View.GONE);
            holder.textViewQuantityDifference.setVisibility(View.GONE);
        }

        return convertView;
    }

    public  void setQuantityAllocatedDisplay(boolean quantityAllocatedDisplay){
        this.quantityAllocatedDisplay = quantityAllocatedDisplay;
    }

    static class ViewHolder {
        TextView textViewCommodityName;
        TextView textViewQuantityAllocated;
        TextView textViewQuantityReceived;
        TextView textViewQuantityDifference;
    }
}
