package uk.ac.kent.fe44.edanews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by fe44 on 20/03/16.
 */
public class PeekDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // inflate the dialog's layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_peek, null);

        //get handles on the views in dialog
        TextView title = (TextView)v.findViewById(R.id.peek_title);
        TextView short_info = (TextView)v.findViewById(R.id.peek_short_info);
        NetworkImageView photo = (NetworkImageView)v.findViewById(R.id.peek_photo);

        //load things from bundle into views
        Bundle args = this.getArguments();
        title.setText(args.getString(ArticlesApp.TITLE));
        short_info.setText(args.getString(ArticlesApp.SHORT_INFO));
        photo.setImageUrl(
                args.getString(ArticlesApp.IMAGE_URL),
                ArticlesApp.getInstance().getImageLoader()
        );

        //build the alertDialog and attach the view
        AlertDialog.Builder dBldr = new AlertDialog.Builder(getActivity());
        dBldr.setView(v);

        //hand back the built dialog
        return dBldr.create();
    }

    /* instantiate the dialog listener */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
