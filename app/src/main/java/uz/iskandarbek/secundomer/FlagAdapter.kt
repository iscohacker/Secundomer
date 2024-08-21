package uz.iskandarbek.secundomer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FlagAdapter(context: Context, private val flags: List<String>) : ArrayAdapter<String>(context, 0, flags) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_flag, parent, false)
        val flagTextView: TextView = view.findViewById(R.id.flagTextView)
        val flag = getItem(position)

        flagTextView.text = flag

        return view
    }
}
