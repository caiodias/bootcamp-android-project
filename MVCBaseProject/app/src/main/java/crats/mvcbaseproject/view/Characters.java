package crats.mvcbaseproject.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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
import crats.mvcbaseproject.model.Character;
import crats.mvcbaseproject.model.Person;

public class Characters extends AppCompatActivity implements AdapterView.OnItemClickListener,ICharacterController {
    ListView customObjectlistView1;

    ArrayList<String> CharactersList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customObjectlistView1 = (ListView) findViewById(R.id.customObjectListView1);

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
        customObjectlistView1.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        customObjectlistView1.setAdapter(adapter);


    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Character Character = CharacterController.shared().getListOfObject().get(position);
        displayDialog(Character);
    }
    public ArrayList<String> getCharactersList(){

        ArrayList<String> CharacterList_name = new ArrayList<String>();
        ArrayList<Character> list = new ArrayList<Character>();
        String name;

        list = CharacterController.shared().getListOfObject();

        for (int i = 0; i <list.size() ; i++) {
            name = list.get(i).getCharName();

            CharacterList_name.add(name);
        }

        return CharacterList_name;
    }
    private void displayDialog(Character obj) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Character " + obj.getCharName());
        alertDialog.setMessage("You selected " + obj.getCharName() + "\nHis/Her available comics are " + obj.getAvailableComics());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    @Override
    public void fetchCharacterSuccess() {
        CharactersList = this.getCharactersList();
        this.setupListView();
    }

    @Override
    public void fetchCharacterFailure(String errorMessage) {

    }
}
