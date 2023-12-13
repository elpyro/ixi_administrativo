package app.castellanoseloy.ixi_administrativo.fragmentos

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle

import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import app.castellanoseloy.ixi_administrativo.R

class ProgressDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): ProgressDialogFragment {
            return ProgressDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.progress_dialog, null)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val messageTextView = view.findViewById<TextView>(R.id.messageTextView)

        messageTextView.text = "Obteniendo Información..." // Puedes personalizar el mensaje aquí

        val dialog = AlertDialog.Builder(requireActivity())
            .setView(view)
            .setCancelable(true)
            .create()

        return dialog
    }
}