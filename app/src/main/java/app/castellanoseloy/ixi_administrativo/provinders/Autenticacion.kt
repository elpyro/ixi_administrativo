package app.castellanoseloy.ixi_administrativo.provinders


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential

class Autenticacion {

    var firebaseAutenticacion=FirebaseAuth.getInstance()
    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    fun login( email:String , clave:String ): Task<AuthResult>{
        return firebaseAutenticacion.signInWithEmailAndPassword(email,clave)
    }
    fun registro( email:String , clave:String ): Task<AuthResult>{
        return firebaseAutenticacion.createUserWithEmailAndPassword(email,clave)
    }
    fun obtenerId():String{
        return firebaseAutenticacion.currentUser?.uid ?: ""
    }
    fun sesionExistente(): Boolean {
        return firebaseAutenticacion.currentUser != null
    }


    fun cambiarClave(newPassword: String): Task<Void>? {
        val usuario = firebaseAutenticacion.currentUser
        return usuario?.updatePassword(newPassword)
    }

    fun obtenerDatosUsuario(): FirebaseUser? {
        return firebaseAutenticacion.currentUser
    }



    fun logout() {
        firebaseAutenticacion.signOut()
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Task<AuthResult> {
        return firebaseAutenticacion.signInWithCredential(credential)
    }


}