package club.frickel.feelathome

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import java.net.Authenticator
import java.net.PasswordAuthentication

class Main : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username:String = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.USERNAME, Constants.USERNAME)
        val password:String = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PASSWORD, Constants.PASSWORD)

        Authenticator.setDefault(object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password.toCharArray())
            }
        })

        setContentView(R.layout.main)
        if (savedInstanceState == null) {
            replaceFragment(DeviceListFragment())
        }
    }

    fun restartApplication() {
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    fun replaceFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    fun replaceFragmentAndAddToBackstack(fragment: Fragment) {

        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuitem_about -> {
                Toast.makeText(applicationContext, "about", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menuitem_settings -> {
                replaceFragmentAndAddToBackstack(Settings())
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }
}
