package filmesomdb.cursoandroid.com.filmesomdb;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import filmesomdb.cursoandroid.com.filmesomdb.Util.Utilidades;

import filmesomdb.cursoandroid.com.filmesomdb.OMDBconexao.Retrofit;
import filmesomdb.cursoandroid.com.filmesomdb.OMDBconexao.Pesquisa;



public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Pesquisa.ResultWithDetail>{


    private Button botaopesquisa;
    private EditText textopesquisa;
    private RecyclerView listarvideorecycler;
    private MovieRecyclerViewAdapter videoadapter;
    private String videotitulo;
    private ProgressBar progressbarid;
    private ImageButton meusfilmes;

    private static final int LOADER_ID = 1;

    private static final String LOG_TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textopesquisa = (EditText) findViewById(R.id.search_edittext);
        meusfilmes = (ImageButton) findViewById(R.id.meusfilmesid);

        textopesquisa.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    startSearch();
                    handled = true;
                }
                return handled;
            }
        });
        botaopesquisa = (Button) findViewById(R.id.search_button);
        listarvideorecycler = (RecyclerView) findViewById(R.id.recycler_view);
        botaopesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
        videoadapter = new MovieRecyclerViewAdapter(null);
        listarvideorecycler.setAdapter(videoadapter);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count), StaggeredGridLayoutManager.VERTICAL);
        listarvideorecycler.setItemAnimator(null);

        listarvideorecycler.setLayoutManager(gridLayoutManager);
        getSupportLoaderManager().enableDebugLogging(true);
        progressbarid = (ProgressBar) findViewById(R.id.progress_spinner);
        meusfilmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MeusFilmesActivity.class));
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mMovieTitle", videotitulo);
        outState.putInt("progress_visibility",progressbarid.getVisibility());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        int progress_visibility= savedInstanceState.getInt("progress_visibility");

        if(progress_visibility == View.VISIBLE) {
            progressbarid.setVisibility(View.VISIBLE);
        }

        videotitulo = savedInstanceState.getString("mMovieTitle");
        if (videotitulo != null) {
            Bundle args = new Bundle();
            args.putString("movieTitle", videotitulo);
            getSupportLoaderManager().initLoader(LOADER_ID, args, this);
        }
    }

    @Override
    public Loader<Pesquisa.ResultWithDetail> onCreateLoader(int id, Bundle args) {
        return new Retrofit(MainActivity.this, args.getString("movieTitle"));
    }

    @Override
    public void onLoadFinished(Loader<Pesquisa.ResultWithDetail> loader, Pesquisa.ResultWithDetail resultWithDetail) {
        progressbarid.setVisibility(View.GONE);
        listarvideorecycler.setVisibility(View.VISIBLE);
        if(resultWithDetail.getResponse().equals("True")) {
            videoadapter.swapData(resultWithDetail.getMovieDetailList());
        } else {
            Snackbar.make(listarvideorecycler,
                    getResources().getString(R.string.snackbar_titulo_nao_encontrado), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Pesquisa.ResultWithDetail> loader) {
        videoadapter.swapData(null);
    }

    public class MovieRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

        private List<Pesquisa.Detail> mValues;

        public MovieRecyclerViewAdapter(List<Pesquisa.Detail> items) {
            mValues = items;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_lista_videos, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final Pesquisa.Detail detail = mValues.get(position);
            final String title = detail.Title;
            final String imdbId = detail.imdbID;
            final String director = detail.Director;
            final String year = detail.Year;
            holder.diretorview.setText(director);
            holder.tituloview.setText(title);
            holder.anoview.setText(year);

            final String imageUrl;
            if (! detail.Poster.equals("N/A")) {
                imageUrl = detail.Poster;
            } else {

                imageUrl = getResources().getString(R.string.default_poster);
            }
            holder.imagemview.layout(0, 0, 0, 0);
            Glide.with(MainActivity.this).load(imageUrl).into(holder.imagemview);

            holder.videosview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Detalhes.class);

                    intent.putExtra(filmesomdb.cursoandroid.com.filmesomdb.Detalhes.MOVIE_DETAIL, detail);
                    intent.putExtra(Detalhes.IMAGE_URL, imageUrl);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(MainActivity.this,
                                    holder.imagemview, "poster");
                    startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mValues == null) {
                return 0;
            }
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View videosview;
            public final TextView tituloview;
            public final TextView anoview;
            public final TextView diretorview;
            public final ImageView imagemview;

            public ViewHolder(View view) {
                super(view);
                videosview = view;
                tituloview = (TextView) view.findViewById(R.id.movie_title);
                anoview = (TextView) view.findViewById(R.id.movie_year);
                imagemview = (ImageView) view.findViewById(R.id.thumbnail);
                diretorview = (TextView) view.findViewById(R.id.movie_director);
            }

        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
            Glide.clear(holder.imagemview);
        }

        public void swapData(List<Pesquisa.Detail> items) {
            if(items != null) {
                mValues = items;
                notifyDataSetChanged();

            } else {
                mValues = null;
            }
        }
    }

    private void startSearch() {
        if(Utilidades.isNetworkAvailable(getApplicationContext())) {
            Utilidades.hideSoftKeyboard(MainActivity.this);
            String movieTitle = textopesquisa.getText().toString().trim();
            if (!movieTitle.isEmpty()) {
                Bundle args = new Bundle();
                args.putString("movieTitle", movieTitle);
                getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                videotitulo = movieTitle;
                progressbarid.setVisibility(View.VISIBLE);
                listarvideorecycler.setVisibility(View.VISIBLE);
            }
            else
                Snackbar.make(listarvideorecycler,
                        getResources().getString(R.string.snackbar_titulo_vazio),
                        Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(listarvideorecycler,
                    getResources().getString(R.string.internet_naohabilitada),
                    Snackbar.LENGTH_LONG).show();
        }
    }

}
