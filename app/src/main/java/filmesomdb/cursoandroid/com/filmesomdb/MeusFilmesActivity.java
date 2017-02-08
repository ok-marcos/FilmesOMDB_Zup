package filmesomdb.cursoandroid.com.filmesomdb;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.*;

public class MeusFilmesActivity extends Activity {

    private Button adicionarid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_filmes);

        adicionarid = (Button)findViewById(R.id.filmenovoid);
        adicionarid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        adicionarid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MeusFilmesActivity.this, Cadastro.class));
            }
        });

    }

}
