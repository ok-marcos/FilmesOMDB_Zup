package filmesomdb.cursoandroid.com.filmesomdb.OMDBconexao;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;


public class Retrofit extends AsyncTaskLoader<Pesquisa.ResultWithDetail> {

    private static final String LOG_TAG = "Retrofit";

    private final String mTitle;

    private Pesquisa.ResultWithDetail mData;

    public Retrofit(Context context, String title) {
        super(context);
        mTitle = title;
    }

    @Override
    public Pesquisa.ResultWithDetail loadInBackground() {

        try {
            Pesquisa.Result result =  Pesquisa.performSearch(mTitle);
            Pesquisa.ResultWithDetail resultWithDetail = new Pesquisa.ResultWithDetail(result);
            if(result.Search != null) {
                for(Pesquisa.Movie movie: result.Search) {
                    resultWithDetail.addToList(Pesquisa.getDetail(movie.imdbID));
                }
            }
            return  resultWithDetail;
        } catch(final IOException e) {
            Log.e(LOG_TAG, "Erro ao acessar API", e);
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {

            deliverResult(mData);
        } else {
            forceLoad();
        }
    }


    @Override
    protected void onReset() {
        Log.d(LOG_TAG, "onReset");
        super.onReset();
        mData = null;
    }

    @Override
    public void deliverResult(Pesquisa.ResultWithDetail data) {
        if (isReset()) {

            return;
        }


        Pesquisa.ResultWithDetail oldData = mData;
        mData = data;

        if (isStarted()) {

            super.deliverResult(data);
        }

    }
}
