package filmesomdb.cursoandroid.com.filmesomdb;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Cadastro extends Activity {

    private EditText filmenome;
    private EditText autor;
    private EditText  anofilme;
    private EditText sinopse;
    private Button   salvar;

    private Spinner  tipofilme;

    private ArrayAdapter<String> tipofilmelista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrofilme);


        filmenome = (EditText)findViewById(R.id.filmenomeid);
        autor = (EditText)findViewById(R.id.autorfilmeid);
        anofilme = (EditText) findViewById(R.id.anoid);
        sinopse = (EditText)findViewById(R.id.sinopseid);
        salvar = (Button) findViewById(R.id.salvarid);

        tipofilme = (Spinner)findViewById(R.id.tipofilmeid);

        tipofilmelista = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        tipofilmelista.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tipofilme.setAdapter(tipofilmelista);

        tipofilmelista.add("Românce");
        tipofilmelista.add("Comédia");
        tipofilmelista.add("Ação");
        tipofilmelista.add("Aventura");
        tipofilmelista.add("Drama");
        tipofilmelista.add("Terror");



    }
}
