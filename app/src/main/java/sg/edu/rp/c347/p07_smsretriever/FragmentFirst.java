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
public class FragmentFirst extends Fragment {

    TextView tvSMS;
    EditText etno;
    Button btnRetrieve;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        tvSMS = view.findViewById(R.id.tvSMS);
        etno = view.findViewById(R.id.etno);
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

                if (etno.getText().length() > 0) {
                    String number = etno.getText().toString();
                    filter = "address LIKE ? AND type = ? ";
                    filterArgs = new String[]{"%" + number + "%", "1"};
                } else {
                    filter = "type = ? ";
                    filterArgs = new String[]{"1"};
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
                tvSMS.setText(smsBody);
            }
        });
        return view;
    }
}
