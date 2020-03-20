package r.sardorbek.mytaxiapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import r.sardorbek.mytaxiapp.Data
import r.sardorbek.mytaxiapp.R
import java.util.*
import kotlinx.android.synthetic.main.row.view.*

class TripAdapter(context: Context?, resource: Int, tripData: List<Data>?) : ArrayAdapter<Data?>(context!!, resource, tripData!!) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        }

        val currentData = getItem(position)
        if (currentData != null && convertView != null) {
            convertView.startingAddress.text = currentData.startingLocations
            convertView.endingAddress.text = currentData.finalLocation
            convertView.price.text = currentData.price
        }

        return convertView!!
    }
}