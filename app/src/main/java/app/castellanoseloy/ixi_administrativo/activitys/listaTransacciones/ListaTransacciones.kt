package app.castellanoseloy.ixi_administrativo.activitys.listaTransacciones

import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.activitys.listaValidarConductores.ListaValidarConductoresViewModel
import app.castellanoseloy.ixi_administrativo.adaptadores.ConductoresListaAdaptador
import app.castellanoseloy.ixi_administrativo.adaptadores.RecargasAdapter
import app.castellanoseloy.ixi_administrativo.databinding.ActivityListaConductorsReferidoresVehiculosBinding
import app.castellanoseloy.ixi_administrativo.modelos.TransaccionesModelo
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.eliminarAcentosTildes
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.formatoMonenda

class ListaTransacciones : AppCompatActivity() {
    private var listaRecargas: java.util.ArrayList<TransaccionesModelo>? = null
    private lateinit var binding: ActivityListaConductorsReferidoresVehiculosBinding

    private lateinit var adapter: RecargasAdapter
    private var viewModel: ListaTransaccionesViewModel? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListaConductorsReferidoresVehiculosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewListaReferidos.layoutManager = linearLayoutManager

        viewModel = ViewModelProvider(this).get(ListaTransaccionesViewModel::class.java)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Lista Transacciones"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        listaRecargas = intent.getSerializableExtra("listaRecargas") as? ArrayList<TransaccionesModelo>
        Log.d("Saldos informacion","la lista de transacciones es ${listaRecargas?.size}")

        viewModel?.setRecargas(listaRecargas)
        listaRecargas?.let { viewModel?.sumarEstados(it) }

        binding.linearLayoutSaldos.visibility=View.VISIBLE

        configurarSpinner()
        observadores()
        listeners()
    }

    private fun configurarSpinner() {
        // Obtener el array de recursos
        val opcionesArray = resources.getStringArray(R.array.estadosNequi)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

    }

    private fun listeners() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Obtiene el valor seleccionado en el Spinner
                val estadoSeleccionado = parentView?.getItemAtPosition(position).toString()

                // Obtiene el texto actual del SearchView
                val busqueda = binding.searchViewBusqueda.query.toString().trim()

                // Ahora puedes usar `estadoSeleccionado` y `busqueda` según tus necesidades
                viewModel?.filtrar(busqueda.eliminarAcentosTildes(), estadoSeleccionado)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Se llama cuando no hay nada seleccionado
            }
        }
        binding.searchViewBusqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    val estadoSeleccionado=binding.spinner.selectedItem.toString()
                    viewModel?.filtrar(newText.eliminarAcentosTildes(),estadoSeleccionado )
                } else {
                    // Si el texto está vacío, muestra todos los clientes
                    viewModel?.setRecargas(listaRecargas)
                    listaRecargas?.let { viewModel?.sumarEstados(it) }
                }
                return true
            }
        })

        //desbloquea searchview al seleccionarlo
        binding?.searchViewBusqueda?.setOnClickListener {
            binding?.searchViewBusqueda?.isIconified=false
        }
        binding?.searchViewBusqueda?.setOnCloseListener {
            // Este bloque de código se ejecutará cuando se haga clic en el ícono de eliminar (X)
            binding.spinner.setSelection(0)
            false // Devuelve 'false' para permitir que el SearchView se cierre normalmente
        }
    }

    private fun observadores() {
        viewModel?.sumasTransacciones?.observe(this){sumaSaldos->
            binding.textViewRealizadas.setText(sumaSaldos.sumaRealizado.toString().formatoMonenda())
            binding.textViewCancelados.setText(sumaSaldos.sumaCancelado.toString().formatoMonenda())
            binding.textViewPendientes.setText(sumaSaldos.sumaPendiente.toString().formatoMonenda())

        }
        viewModel?.transacciones?.observe(this) { listaTransacciones ->
            adapter = RecargasAdapter(
                this@ListaTransacciones,
                listaTransacciones
            )
            binding.recyclerViewListaReferidos.adapter = adapter
        }
    }
}