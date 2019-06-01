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
import android.widget.EditText;
import android.widget.TextView;

import com.example.firebase.Models.Item;
import com.example.firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormFragment extends Fragment {
    String key = "";
    String TAG = "FormFragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("ItemID")) {
            key = bundle.getSerializable("ItemID").toString();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        if(!key.isEmpty()){
            fillElements(view);
        }
        updateButtonsStatus(key, view);
        setupUI(view);
        return view;
    }

    private void fillElements(final View view){
        db.collection("items").document(key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            TextView txtName = view.findViewById(R.id.txtItemNameForm);
                            txtName.setText(document.get("name").toString());

                            TextView txtQuantity = view.findViewById(R.id.txtQuantityForm);
                            txtQuantity.setText(document.get("quantity").toString());

                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void updateButtonsStatus(String pKey, View view){
        //Si item existe mostrar delete button
        if(!pKey.isEmpty()){
            FloatingActionButton btnDelete = view.findViewById(R.id.btnDelete);
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    private void setupUI (final View view){
        //Guardar item button
        FloatingActionButton btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save information and open back Fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                MainFragment fragmentListView = new MainFragment();

                //Save fireBase object
                saveFirebaseObject(view);

                ft.replace(android.R.id.content, fragmentListView);
                ft.addToBackStack(null); //Add fragment in back stack
                ft.commit();
            }
        });

        //Borrar item button
        FloatingActionButton btnDelete = view.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save information and open back Fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                MainFragment fragmentListView = new MainFragment();

                //Delete fireBase object
                deleteFirebaseObject(view);

                ft.replace(android.R.id.content, fragmentListView);
                ft.addToBackStack(null); //Add fragment in back stack
                ft.commit();
            }
        });
    }

    private void saveFirebaseObject(View view){
        //Collect information
        Item item = collectInfo(view); //Our Item Object

        // Get item
        Map<String, Object> user = new HashMap<>();
        user.put("name", item.getName());
        user.put("quantity", item.getQuantity());

        if(key.isEmpty()) {
            // Add a new document with a generated ID
            db.collection("items")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
        else {
            //Update current document with new information
            db.collection("items").document(key)
                    .update(user)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
        }
    }

    private void deleteFirebaseObject(View view){
        db.collection("items").document(key)
              .delete();
    }

    private Item collectInfo(View view){
        String name =  ((EditText) view.findViewById(R.id.txtItemNameForm)).getText().toString();
        int quantity =  Integer.parseInt(((EditText) view.findViewById(R.id.txtQuantityForm)).getText().toString());
        Item item = new Item(name,quantity,"");

        return item;
    }
}