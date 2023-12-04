package Inventario.app.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Inventario.app.Model.Fire;
import Inventario.app.R;
import Inventario.app.ViewModel.InterfazInventario;
import Inventario.app.ViewModel.RegistrarObj;
import Inventario.app.ViewModel.item;
import Inventario.app.databinding.ActivityInterfazInventarioBinding;
import Inventario.app.databinding.FragmentGalleryBinding;
import Inventario.app.prueba;

public class GalleryFragment extends Fragment {

    private ActivityInterfazInventarioBinding binding;
    private ArrayList<String> lista=new ArrayList<String>();
    ArrayAdapter<String> adapt;
    item qr;

    Button registrarobg,registrarcli,edit,elim;
    GridView obj;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityInterfazInventarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        registrarcli=binding.interfazinvregistrarcli;
        registrarobg=binding.interfazinvregistrarobj;
        edit=binding.interfazinvedit;
        elim=binding.interfazinvelim;
        obj=binding.interfazinvtabla;

        estadistico();
        registrarobg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), RegistrarObj.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) parent.getAdapter();
                int numColumnas = obj.getNumColumns();
                int ini = position - (position % numColumnas);
                qr=new item();
                qr.setNombre(arrayAdapter.getItem(ini));
                qr.setReferencia(arrayAdapter.getItem(ini+1));
                try {
                    qr.setCatidad(Integer.parseInt(arrayAdapter.getItem(ini+2)));
                    qr.setPrecio(Integer.parseInt(arrayAdapter.getItem(ini+3)));
                } catch (NumberFormatException e) {
                }
            }
        });
        return root;
    }

    private void estadistico() {
        Fire.dr.child("usuario").child(Fire.user.getUid()).child("inventario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dat:snapshot.getChildren()){
                        item Item=new item();
                        Item.setCatidad(dat.child("catidad").getValue(int.class));
                        Item.setCosto(dat.child("costo").getValue(int.class));
                        Item.setNombre(dat.child("nombre").getValue(String.class));
                        Item.setPrecio(dat.child("precio").getValue(int.class));
                        Item.setProveedor(dat.child("proveedor").getValue(String.class));
                        Item.setReferencia(dat.child("referencia").getValue(String.class));
                        lista.add(Item.getNombre());
                        lista.add(Item.getReferencia());
                        lista.add(String.valueOf(Item.getCatidad()));
                        lista.add(String.valueOf(Item.getPrecio()));
                        adapt=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,lista);
                        obj.setAdapter(adapt);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}