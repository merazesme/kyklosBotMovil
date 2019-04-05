package com.example.esmeralda.kyklosbotmovil;

import android.support.design.widget.BottomSheetDialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BottomSheetMenuFragment extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grid_bottom_sheet, container, false);

        ImageView uno = v.findViewById(R.id.uno);
        ImageView dos = v.findViewById(R.id.dos);
        ImageView tres = v.findViewById(R.id.tres);
        ImageView cuatro = v.findViewById(R.id.cuatro);
        ImageView cinco = v.findViewById(R.id.cinco);
        ImageView seis = v.findViewById(R.id.seis);
        ImageView siete = v.findViewById(R.id.siete);
        ImageView ocho = v.findViewById(R.id.ocho);
        ImageView nueve = v.findViewById(R.id.nueve);
        ImageView diez = v.findViewById(R.id.diez);
        ImageView once = v.findViewById(R.id.once);
        ImageView doce = v.findViewById(R.id.doce);
        TextView eliminar = v.findViewById(R.id.eliminar);

        uno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("uno");
                dismiss();
            }
        });
        dos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("dos");
                dismiss();
            }
        });
        tres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("tres");
                dismiss();
            }
        });
        cuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("cuatro");
                dismiss();
            }
        });
        cinco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("cinco");
                dismiss();
            }
        });
        seis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("seis");
                dismiss();
            }
        });
        siete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("siete");
                dismiss();
            }
        });
        ocho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("ocho");
                dismiss();
            }
        });
        nueve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("nueve");
                dismiss();
            }
        });
        diez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("diez");
                dismiss();
            }
        });
        once.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("once");
                dismiss();
            }
        });
        doce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("doce");
                dismiss();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("eliminar");
                dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
