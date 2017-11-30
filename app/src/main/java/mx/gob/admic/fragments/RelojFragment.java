package mx.gob.admic.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.gob.admic.R;

/**
 * Created by Uriel on 11/03/2016.
 */
public class RelojFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_reloj, container, false);
        getDialog().setTitle("Configurar alarma");
        return rootView;
    }
}
