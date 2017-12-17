package crats.mvcbaseproject.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import crats.mvcbaseproject.R;
import crats.mvcbaseproject.controller.CharacterController;
import crats.mvcbaseproject.controller.ICharacterController;

import crats.mvcbaseproject.controller.PersonController;
import crats.mvcbaseproject.model.Person;

public class Characters extends AppCompatActivity implements AdapterView.OnItemClickListener,ICharacterController {
    ListView customObjectlistView;

    ArrayList<String> CharactersList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customObjectlistView = (ListView) findViewById(R.id.customObjectListView);

        CharacterController.shared().setupCharacterController((ICharacterController) this, this.getBaseContext());
       CharacterController.shared().fetchList();
    }
    private void setupListView(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getBaseContext(),android.R.layout.simple_list_item_1,this.CharactersList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);return textView;
            }
        };
        customObjectlistView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        customObjectlistView.setAdapter(adapter);


    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void fetchCharacterSuccess() {

    }

    @Override
    public void fetchCharacterFailure(String errorMessage) {

    }
}
