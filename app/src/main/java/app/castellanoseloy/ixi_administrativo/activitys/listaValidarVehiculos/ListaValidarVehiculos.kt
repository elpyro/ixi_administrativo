package app.castellanoseloy.ixi_administrativo.activitys.listaValidarVehiculos

import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.activitys.listaValidarConductores.ListaValidarVehiculosViewModel
import app.castellanoseloy.ixi_administrativo.adaptadores.VehiculosListaAdaptador
import app.castellanoseloy.ixi_administrativo.databinding.ActivityListaConductorsReferidoresVehiculosBinding
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.eliminarAcentosTildes

class ListaValidarVehiculos : AppCompatActivity() {

    private lateinit var binding: ActivityListaConductorsReferidoresVehiculosBinding

    private lateinit var adapter: VehiculosListaAdaptador
    private var viewModel: ListaValidarVehiculosViewModel? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListaConductorsReferidoresVehiculosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewListaReferidos.layoutManager = linearLayoutManager

        viewModel = ViewModelProvider(this).get(ListaValidarVehiculosViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Lista Vehiculos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        configurarSpinner()

        observadores()
        listeners()
    }

    private fun configurarSpinner() {
        // Obtener el array de recursos
        val opcionesArray = resources.getStringArray(R.array.estadosVehiculo)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

    }


    fun mostrarDialogProgreso(titulo: String) {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(titulo)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Espere un momento...")
        progressDialog.show()
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
                    viewModel?.getVehiculos()
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


        viewModel?.vehiculos?.observe(this) { listaVehiculos ->
            adapter = VehiculosListaAdaptador(
                this@ListaValidarVehiculos,
                listaVehiculos
            )
            binding.recyclerViewListaReferidos.adapter = adapter
            progressDialog.dismiss()
        }

    }


    override fun onResume() {
        super.onResume()
        mostrarDialogProgreso("Cargando Vehiculos...")
        viewModel?.getVehiculos()
    }

}