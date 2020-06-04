package sg.edu.rp.c347.p07_smsretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSecond extends Fragment {

   TextView tvword;
   EditText etword;
   Button btnRetrieve;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        tvword = view.findViewById(R.id.tvword);
        etword = view.findViewById(R.id.etword);
        btnRetrieve = view.findViewById(R.id.btnRetrieve);

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create all messages URI
                Uri uri = Uri.parse("content://sms");

                // The columns we want
                // date is when the message took place
                // address is the number of the other party
                // body is the message content
                // type 1 is received, type 2 sent
                String[] reqCols = new String[]{"date", "address", "body", "type"};

                String filter;
                String[] filterArgs;

                if (etword.getText().length() > 0) {
                    filter = "";
                    String str_keywords = "rain shine";
                    filterArgs = str_keywords.split(" ");
                    for (int i = 0; i < filterArgs.length; i++) {
                        filter += " body LIKE ? ";
                        if (i+1<filterArgs.length) {
                            filter += " OR ";
                        }
                        filterArgs[i] = "%" + filterArgs[i] + "%";
                    }
                } else {
                    filter = null;
                    filterArgs = null;
                }

                // Get Content Resolver object from which to
                // query the content provider
                ContentResolver cr = getActivity().getContentResolver();
                // Fetch SMS Message from Built-in Content Provider
                Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);

                String smsBody = "";
                if (cursor.moveToFirst()) {
                    do {
                        Long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat
                                .format("dd MM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox:";
                        } else {
                            type = "Sent:";
                        }
                        smsBody += type + " " + address + "\n at " + date + "\n\"" + body + "\"\n\n";
                    } while (cursor.moveToNext());
                }
                tvword.setText(smsBody);
            }
        });
        return view;
    }
}
