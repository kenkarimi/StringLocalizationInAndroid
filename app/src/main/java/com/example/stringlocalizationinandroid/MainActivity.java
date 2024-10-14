package com.example.stringlocalizationinandroid;

import android.content.res.Configuration;
import android.icu.text.PluralRules;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tvDefaultLocale, tvCustomerName, tvStock, tvPrice, tvSongs, tvPersons, tvStudents;
    //What to do when you can't use an enum because you expect a value to saved in the database so it needs to stay a string/int etc., while at the same time offering you the benefits of having it typed for several string options.
    private String gender = Gender.DEFAULT; //Problem with this method is I can still assign any string to this gender variable because its type is String.
    private OrderStatus order_status = OrderStatus.DEFAULT; //instead of having to do order_status = new OrderStatus("") we've created static final variables assigned to = new OrderStatus("")

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDefaultLocale = (TextView) findViewById(R.id.tvDefaultLocale);
        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        tvStock = (TextView) findViewById(R.id.tvStock);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvSongs = (TextView) findViewById(R.id.tvSongs);
        tvPersons = (TextView) findViewById(R.id.tvPersons);
        tvStudents = (TextView) findViewById(R.id.tvStudents);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("MainActivity");

        /**
         * Can't compare order_status directly to primitive data type values("", "Complete", "Incomplete") because the variable order_status is an instance of the OrderStatus class(new OrderStatus("")).
         * order_status comparisons have to be made against the static final variables that are themselves instances of the OrderStatus class.
         * This also means that you can't send the order_status variable to be saved to your database as its not a string.
         * To get the string value, do: String this_string = order_status.getValue() which is essentially String this_string = new OrderStatus("").getValue();
         */
        if(order_status.equals(OrderStatus.DEFAULT)) { //You alternatively compare to OrderStatus.DEFAULT
            Log.d("OrderStatus", String.valueOf(order_status)); //conversion doesn't work.
            Log.d("OrderStatus", order_status.getValue()); //Will be empty in logcat
        } else if(order_status.equals(OrderStatus.COMPLETE)){ //You alternatively compare to OrderStatus.COMPLETE
            Log.d("OrderStatus", String.valueOf(order_status)); //conversion doesn't work.
            Log.d("OrderStatus", order_status.getValue());
        } else if(order_status.equals(OrderStatus.INCOMPLETE)){ //You alternatively compare to OrderStatus.INCOMPLETE
            Log.d("OrderStatus", String.valueOf(order_status)); //conversion doesn't work.
            Log.d("OrderStatus", order_status.getValue());
        } else {
            Log.d("OrderStatusFailure", "All comparisons failed...");
        }

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
        String default_locale = String.format(getResources().getString(R.string.default_locale), country, dispCountry); //Two format specifier arguments provided because we use the format specifier %s twice in the same string.
        tvDefaultLocale.setText(default_locale);

        //Change default language.
        Locale locale = new Locale("es"); //use "jp" for japanese and "es" for spanish and "en" to use the default language strings.xml.
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

        /*Pluralization in Android can be done using two methods: Using the @plurals resource or using the PluralRules class. Here, we only cover the first method as it's preferred for its straightforwardness and ease of maintenance.*/
        //Using the @plurals resource in your strings.xml file:
        int songs = 1;
        String plural_songs = getResources().getQuantityString(R.plurals.songs, songs, songs); //Argument 2 is quantity(plural form we want to use in strings.xml). Argument 3 is formatArgs(format specifier value). Both should be the same value as one determines the other. A mismatch will still work but the string won't make sense. e.g. If quantity is provided as 1 and formatArgs as 2, the string will read: "You have 2 song."
        tvSongs.setText(plural_songs);

        /**
         * IMPORTANT: This example assumes that your default is set to en/default or es.
         * While in the above example, the integer value of songs(1), provided as the quantity in argument in getQuantityStrings() maps to the "one" item in the default strings.xml(en) and any other value maps to "other",
         * This doesn't mean that these int values(0,1,2,3) would map directly to("zero","one","two","other") respectively in strings.xml if the items were provided.
         * Providing additional items like "zero" and "two" like we've done in the example below(R.plurals.persons), even if int persons = 0, this quantity will match to "other" instead of "zero".
         * Meaning that tvPersons will read "[other] 0 people." instead of "[zero] 0 people."
         * This is because the grammatical structure of a sentence in English/Spanish only changes for 1 and is the same for any other value(0,2,3 etc). E.g. "1 Person" vs "0/2/3/etc People." As such, the only plural items we execute are 'one' and 'other'. Every other plural item is ignored even if defined.
         * These are referred to as quantity classes. A quantity class is a set of quantity values that have the same grammatical rules in a given language. In this case, 1 is its own quantity class and every other value is in the 'other' quantity class.
         * This crucially means that which quantity classes are used, and which numeric values are mapped to them depends on the locale the respective resource file is for.
         * In Japanese, Chinese & Korean, only other is used, because in these languages sentences don't grammatically differ based on the given quantity.
         * As such, the zero, one, two plural rule items are ignored even if defined and 'other' is executed. E.g. If quantity is 1, the output will be "[other] 1 people. (JP)" instead of "[one] 1 person. (JP)"
         * In Irish: 1 is mapped to one, 2 is mapped to two, 3-6 is few, 7-10 is many, 0 and 11+ is other.
         * More: https://stackoverflow.com/questions/41950952/how-to-use-android-quantity-strings-plurals
         */
        int persons = 3;
        String plural_persons = getResources().getQuantityString(R.plurals.persons, persons, persons);
        tvPersons.setText(plural_persons);

        //For niche scenarios where you e.g. you need to put what would be superscripts after a number e.g. st,nd,rd,th but you can't rely on multiple plural items/forms because of the language e.g. english, spanish, japanese chinese or korean.
        //Also note: format specifier positioning is indicated with a number & dolar sign in between the format specifier. e.g. %2$d.
        int students = 2;
        String superscript = students == 1 ? "st" : students == 2 ? "nd" : students == 3 ? "rd" : "th";
        String plural_students = getResources().getQuantityString(R.plurals.students, students, students, superscript);//Two format specifier arguments provided because we use the format specifier %d and %s in the same string.
        tvStudents.setText(plural_students);
    }
}
