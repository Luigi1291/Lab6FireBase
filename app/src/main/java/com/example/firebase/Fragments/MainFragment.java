package com.example.firebase.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.firebase.Adapters.ItemAdapter;
import com.example.firebase.Models.Item;
import com.example.firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    String TAG= "MainFragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        setupUI(view);

        //Close input method when open
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        return view;
    }

    private void setupUI (View view) {
        DisplayListItems(view);

        //Formulario para agregar item en Firebase
        FloatingActionButton btnAddDB =  (FloatingActionButton) view.findViewById(R.id.btnAddDB);
        btnAddDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                FormFragment fragmentForm = new FormFragment();
                ft.replace(android.R.id.content, fragmentForm);
                ft.addToBackStack(null); //Add fragment in back stack
                ft.commit();
            }
        });

        final ListView listViewItems=  (ListView) view.findViewById(R.id.listViewItems);
        listViewItems.setClickable(true);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Item item = (Item) listViewItems.getItemAtPosition(position);

                        //Send information to another Fragment
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        FormFragment fragmentListView = new FormFragment();
                        Bundle bundle = new Bundle();
                        String obj = item.getKey() ; //Our Persona Object
                        bundle.putSerializable("ItemID", obj);
                        fragmentListView.setArguments(bundle);
                        ft.replace(android.R.id.content, fragmentListView);
                        ft.addToBackStack(null); //Add fragment in back stack
                        ft.commit();
                    }
                });
    }

    private void DisplayListItems(final View pView){
        final ArrayList<Item> mListItems = new ArrayList<>();

        db.collection("items")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Convertir objeto recibido
                            Item item = new Item(
                                    document.get("name").toString(),
                                    Integer.parseInt(document.get("quantity").toString()),
                                    document.getId()
                            );
                            mListItems.add(item);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }

                        //Actualizar información
                        int layoutId = R.layout.list_element_item;
                        ItemAdapter adapter = new ItemAdapter(pView.getContext() ,layoutId, mListItems);

                        ListView mListView = (ListView) pView.findViewById(R.id.listViewItems);
                        mListView.setAdapter(adapter);

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }
}
