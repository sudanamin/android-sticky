package com.example.aminov.aaa

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sticky_ticket.view.*

class MainActivity : AppCompatActivity() {
var listOfStickys = ArrayList<Sticky>()
    var adapter : StickyAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //var textview:TextView =findViewById<TextView>(R.id.tt)
        //textview.setText("welcome amin you are doing well")
        loadSticky()

        adapter = StickyAdapter(listOfStickys,this)
        sticky_listview.adapter = adapter
    }

    fun loadSticky(){
        listOfStickys.add(Sticky("note1","asdf","label1" ))
        listOfStickys.add(Sticky("note2","asdf","label2" ))
        listOfStickys.add(Sticky("note3","asdf","label3" ))
        listOfStickys.add(Sticky("note4","asdf","label4" ))

    }


    class StickyAdapter:BaseAdapter{
          var listOfStickyLocal = ArrayList<Sticky>()
        var context:Context?=null

        constructor(listOfSticky:ArrayList<Sticky>,context: Context){
            listOfStickyLocal = listOfSticky
            this.context = context


        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
           var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val sticky = listOfStickyLocal[p0]
            var stickView = inflator.inflate(R.layout.sticky_ticket,null)
            stickView.title_textview.setText(sticky.title!!)
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                stickView.data_textview.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"))
            }
            else {
                stickView.title_textview.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>", Html.FROM_HTML_MODE_COMPACT))

            }


            stickView.label_textview.setText(sticky.label!!)

           return stickView
        }

        override fun getItem(p0: Int): Any {
           return  listOfStickyLocal[p0]
        }

        override fun getItemId(p0: Int): Long {
            return  p0.toLong()
        }

        override fun getCount(): Int {
            return listOfStickyLocal.size
        }
    }
}



