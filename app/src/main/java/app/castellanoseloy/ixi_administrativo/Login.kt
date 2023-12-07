package app.castellanoseloy.ixi_administrativo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.castellanoseloy.ixi_administrativo.modelos.UsuarioModelo
import app.castellanoseloy.ixi_administrativo.provinders.Autenticacion
import app.castellanoseloy.ixi_administrativo.servicios.DatosPersitidos
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

val RC_SIGN_IN = 123 //inicio sesion gmail


private val autenticacion = Autenticacion()

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


    }

    private fun autenticacionGoogle() {

        autenticacion.firebaseAutenticacion = FirebaseAuth.getInstance()

        autenticacion.authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // El usuario ha iniciado sesión
                val nombreUsuario = user.displayName
                val correoUsuario = user.email

                // Realizar las acciones necesarias con el nombre y correo del usuario
                Log.d("Firebase autenticacion", "el id google es ${nombreUsuario.toString()}")

            } else {
                inicioGoogle()
            }
        }
        // Registrar el listener
        autenticacion.firebaseAutenticacion.addAuthStateListener(autenticacion.authStateListener)
    }

    private fun inicioGoogle() {
        // El usuario no ha iniciado sesión o ha cerrado sesión
        // Iniciar el flujo de inicio de sesión

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onResume() {
        super.onResume()
        if (autenticacion.sesionExistente()) {
            val usuario= autenticacion.obtenerDatosUsuario()
            val usuarioModelo=UsuarioModelo(
                id= usuario?.uid,
                correo =usuario?.email,
                imagen = usuario?.photoUrl,
                nombre = usuario?.displayName
            )
            DatosPersitidos.datosUsuario=usuarioModelo

            val i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }else{
            inicioGoogle()
        }
    }
}