package app.castellanoseloy.ixi_administrativo.activitys.reportes

import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import app.castellanoseloy.ixi_administrativo.databinding.ActivitySaldosBinding
import app.castellanoseloy.ixi_administrativo.fragmentos.ProgressDialogFragment
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades

class Reportes : AppCompatActivity() {
    private lateinit var binding: ActivitySaldosBinding


    private var viewModel: ReportesViewModel? = null
    private lateinit var progressDialogFragment: ProgressDialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySaldosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this)[ReportesViewModel::class.java]


        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Reportes"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        progressDialogFragment = ProgressDialogFragment.newInstance()

        binding?.textViewDesde?.text = Utilidades.obtenerFechaActual()


        listenes()
    }

    private fun listenes() {

        binding?.textViewDesde?.setOnClickListener {
            viewModel?.mostrarDatePickerDialog(binding?.textViewDesde!!, this)
        }

        binding?.textViewHasta?.setOnClickListener {
            viewModel?.mostrarDatePickerDialog(binding?.textViewHasta!!, this)
        }

        binding?.buttonInforme?.setOnClickListener {
            mostrarProgressDialog()
            viewModel?.mostrarReporte(binding, binding.spinnerTipoReporte.selectedItem.toString(),this) }
    }

    private fun mostrarProgressDialog() {
        progressDialogFragment.show(supportFragmentManager, "ProgressDialog")
    }

    override fun onPause() {
        super.onPause()
        if (progressDialogFragment.isAdded) {
            progressDialogFragment.dismiss()
        }

    }

}