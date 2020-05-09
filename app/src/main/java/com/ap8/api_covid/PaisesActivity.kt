package com.ap8.api_covid

import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_paises.*


class PaisesActivity : AppCompatActivity() {

    private var estatisticasList = mutableListOf<Estatisticas>()
    var array_paises = ArrayList<String>()
    var adapter = Adapter_Dados(array_paises)
    private var asyncTask: EstatisticasTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paises)

        carregarEstatisticas()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView  = MenuItemCompat.getActionView(searchViewItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                /*   if(list.contains(query)){
                       adapter.getFilter().filter(query);
                   }else{
                       Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                   }*/
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //adapter.getFilter().filter(newText)
                adapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }


    private fun carregarEstatisticas() {
        estatisticasList.clear()
        if(asyncTask == null) {
            if(EstatisticasHTTP.hasConnection(this)) {
                if(asyncTask?.status != AsyncTask.Status.RUNNING) {
                    asyncTask = EstatisticasTask()
                    asyncTask?.execute()
                }
            }
        }
    }

    inner class EstatisticasTask: AsyncTask<Void, Void, List<Estatisticas>>() {
        override fun doInBackground(vararg params: Void?): List<Estatisticas>? {
            val path = "/countries"
            return EstatisticasHTTP.loadEstatisticas(path)
        }
        override fun onPostExecute(resultado: List<Estatisticas>?) {
            super.onPostExecute(resultado)
            atualizarEstatisticas(resultado)
        }
    }

    private fun atualizarEstatisticas(resultado: List<Estatisticas>?) {
        if(resultado != null) {
            this.estatisticasList.clear()
            this.estatisticasList.addAll(resultado)
            getPaises()
            initRecycler()
        }
    }

    fun getPaises() {
        for(index in 0 .. estatisticasList.size - 1) {
            val elemento = estatisticasList[index]
            array_paises.add(elemento.country.toString())
        }
    }

    fun initRecycler() {
        rv.adapter = adapter
        val layout = LinearLayoutManager(this)
        rv.layoutManager = layout
    }

}
