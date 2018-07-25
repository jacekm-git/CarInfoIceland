package `is`.markowski.jacek.vehicle.registration.plates.of.iceland

import `is`.markowski.jacek.vehicle.registration.plates.of.iceland.util.BrandLogo
import `is`.markowski.jacek.vehicle.registration.plates.of.iceland.util.Query
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_search.isEnabled = true
        bt_search.setOnClickListener { v ->
            val plateNumber: String = ed_search_number.text.toString()
            Thread.sleep(100)
            Query.query(this,
                    plateNumber,
                    tv_brand_name,
                    tv_registration_number,
                    tv_alias,
                    tv_vin,
                    tv_first_registered,
                    tv_emmision,
                    tv_weight,
                    tv_status,
                    tv_next_inspection)
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }

        tv_brand_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val brand = getBrandName(p0)
                val source = "http://www.carlogos.org/logo/$brand-logo.png"
                BrandLogo.displayImage(this@MainActivity, source, img_brand)

            }
        })

        ibt_copy_vin.setOnClickListener { _ ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("vin", tv_vin.text.toString())
            clipboard.primaryClip = clip
            Toast.makeText(this@MainActivity, "VIN copied to clipboard!", LENGTH_LONG).show()
        }
    }

    private fun getBrandName(p0: Editable?): String {
        val split = p0?.split(" - ") ?: List(1, { "" })
        val brandWords = split[0].replace("-", " ")
        val splitWords = brandWords.split(" ")
        var brand = ""
        splitWords.forEach({ it -> brand += "-${it.toLowerCase().capitalize()}" })
        brand = brand.substring(1)
        if (brand.length <= 3) { // BMW
            brand = brand.toUpperCase()
        }
        return brand
    }
}