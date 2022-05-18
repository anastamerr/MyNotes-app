package com.anast.mynotes

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.notesticket.view.*

class MainActivity : AppCompatActivity() {
    var ListOfNotes = ArrayList<Notes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // ListOfNotes.add(Notes(1,"Anas fuck","A story where anas fucks himeslef"))
         //ListOfNotes.add(Notes(2,"lol fuck","A story where anas fucks himeslef"))
        //ListOfNotes.add(Notes(3,"bitch fuck","A story where anas fucks himeslef"))

        // load from db
       // var dbManager = DataBaseManager(this)
        loadfromquery("%")


    }

    override fun onResume() { // to be able to refresh
        loadfromquery("%")
        super.onResume()
    }

    override fun onStart(){
        super.onStart()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
    }






    fun loadfromquery(title:String){
        val projections = arrayOf("ID","Title","Description")
        var Dbmanager = DataBaseManager(this)
        val selectionargs = arrayOf(title)
        val cursor = Dbmanager.query(projections,"Title like ?",selectionargs,"Title") // null means nothing specific return everything , title like ? returns accroding to the title// sort order title sorts according to the title
        ListOfNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val ID =cursor.getInt(cursor.getColumnIndex("ID"))
                val Title =cursor.getString(cursor.getColumnIndex("Title"))
                val Description =cursor.getString(cursor.getColumnIndex("Description"))
                ListOfNotes.add(Notes(ID,Title,Description))

            }while (cursor.moveToNext())
        }
        // set the adapter
        var adapter = myadapter(this,ListOfNotes)    // the adapter class intializes whatever between the brackets and makes the adapter an instance
        listviewnotes.adapter = adapter  // need to intialise the adapter


    }




    inner class myadapter: BaseAdapter{
       var ListOfNotesAdapter = ArrayList<Notes>()
        var context:Context?=null
        constructor(context: Context,ListOfNotesAdapter:ArrayList<Notes>):super(){
            this.ListOfNotesAdapter = ListOfNotesAdapter
            this.context = context



        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
          var View = layoutInflater.inflate(R.layout.notesticket,null) // the viewer that displays the item
            var Mynote = ListOfNotesAdapter[position]   // the item that needs to be viewed
            View.title.text = Mynote.noteName
            View.description.text = Mynote.noteDes // my view takes the data from my note to display it
            View.DelButton.setOnClickListener(android.view.View.OnClickListener {
                var dbManager = DataBaseManager(this.context!!)
                val selectionArgs = arrayOf(Mynote.noteID.toString())
                dbManager.Delete("ID=?",selectionArgs)
                // you have to call load from query so its updated
                loadfromquery("%")
            })
            View.edit.setOnClickListener(android.view.View.OnClickListener {
               IntentActivity(Mynote) //takes two parameters (the activity[this] , and the activity you want to go to)

            })


            return View
        }

        override fun getItem(position: Int): Any {
          return ListOfNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
           return ListOfNotesAdapter.size    // so it calls the getView by the number of elements ^
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // search is implemented here so i can look through the data
        menuInflater.inflate(R.menu.main_menu,menu)   // everything needs an inflator to show takes 2 parameters first is the location second is
        //whatever between the function () in this case menu
        //to create a search engine in your APP
        val searchview = menu!!.findItem(R.id.search).actionView as SearchView // views the search and passes the text in search bar
        val searchmanager = getSystemService(Context.SEARCH_SERVICE) as SearchManager // manages the search begins tp search
        searchview.setSearchableInfo(searchmanager.getSearchableInfo(componentName)) //to make it accessiable
        searchview.setOnQueryTextListener(object :SearchView.OnQueryTextListener{    // the method that actually listens and search to the text submitted

            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_LONG).show()
               loadfromquery("%" + query+ "%")

                return false


            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // TODO(searches as the user types
                return false

            }

        })





        return super.onCreateOptionsMenu(menu)
        //inflate means show



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!=null) {
            when (item.itemId) {       // when the item id equals # that means the user clicked on something specific so it fires the command
                R.id.addnote -> {
                    //go the add note page through the IPC-> Inter process communication by intent
                    val intent = Intent(this,Addnotes::class.java)//takes two parameters (the activity[this] , and the activity you want to go to)
                    startActivity(intent)
                }


            }
        }
        return super.onOptionsItemSelected(item)
    } // activates the code when an item from the options is selected


    fun IntentActivity(note:Notes){
        val intent = Intent(this,Addnotes::class.java)//takes two parameters (the activity[this] , and the activity you want to go to)
        intent.putExtra("ID",note.noteID)
        intent.putExtra("Title",note.noteName)
        intent.putExtra("Description",note.noteDes)
        startActivity(intent)
    }




}


