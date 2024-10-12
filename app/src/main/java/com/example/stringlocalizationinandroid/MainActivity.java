package com.example.stringlocalizationinandroid;

import android.content.res.Configuration;
import android.icu.text.PluralRules;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tvDefaultLocale, tvCustomerName, tvStock, tvPrice, tvSongs;
    //What to do when you can't use an enum because you expect a value to saved in the database so it needs to stay a string/int etc., while at the same time offering you the benefits of having it typed for several string options.
    private String gender = Gender.DEFAULT; //Problem with this method is I can still assign any string to this gender variable because its type is String.
    private OrderStatus order_status = OrderStatus.DEFAULT;
    private String o = "Accepted";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDefaultLocale = (TextView) findViewById(R.id.tvDefaultLocale);
        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        tvStock = (TextView) findViewById(R.id.tvStock);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvSongs = (TextView) findViewById(R.id.tvSongs);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("MainActivity");

        /**
         * To include localizations for different languages, you'll need to create different language-specific directories e.g. res/values-en and res/values-es.
         * Within each will be a strings.xml file where you'll define the strings you want to localize using the same resource names as in your default strings.xml located in the res/values directory.
         * To create these directories, right click on the strings directory, go to New>Values Resource File>Name the file strings.xml>Change the directory name from 'values' to 'values-es'>Click "OK".
         * These directories should follow the ISO 639-1 language codes. Translate the strings.
         * Set the preferred language at runtime using the Locale class and the Configuration class.
         * Use Locale.getDefault() method to get the user's default language.
         */

        //Get & print default language.
        Locale defaultLocale = Locale.getDefault();
        String country = defaultLocale.getCountry();
        String dispCountry = defaultLocale.getDisplayCountry();
        String default_locale = String.format(getResources().getString(R.string.default_locale), country, dispCountry); //Two format specifier arguments provided because we use the format specifer %s twice in the same string.
        tvDefaultLocale.setText(default_locale);

        //Change default language.
        Locale locale = new Locale("jp"); //use "jp" for japanese and "es" for spanish and "en" to use the default language strings.xml.
        //Locale locale = Locale.getDefault(); //Alternatively, to set the default language as preferred instead of using "en". (Will use original strings.xml)
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        String name = String.format(getResources().getString(R.string.customer_name), "Jon Doe"); //Argument 2 is the format specifier value.
        tvCustomerName.setText(name);

        String stock = String.format(getResources().getString(R.string.stock), 405); //Argument 2 is the format specifier value. For longs, use 405L
        tvStock.setText(stock);

        String price = String.format(getResources().getString(R.string.price), 999.99f); //Argument 2 is the format specifier value. For doubles, use 999.99
        tvPrice.setText(price);

        /*Pluralization in Android can be done using two methods: Using the @plurals resource or using the PluralRules class. Here, we only cover the first method as it's preffered for its straightforwardness and ease of maintenance.*/
        //Using the @plurals resource in your strings.xml file:
        int songs = 1;
        String plural_songs = getResources().getQuantityString(R.plurals.songs, songs, songs); //Argument 2 is quantity(plural form we want to use in strings.xml). Argument 3 is formatArgs(format specifier value). Both should be the same value as one determines the other. A mismatch will still work but the string won't make sense. e.g. If quantity is provided as 1 and formatArgs as 2, the string will read: "You have 2 song."
        tvSongs.setText(plural_songs);
    }
}
