package ar.edu.unc.famaf.redditreader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.Toast;

import ar.edu.unc.famaf.redditreader.backend.DBAdapter;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Buttons {
    PostModel model;
    PostModelHolder holder;
    DBAdapter db;
    int clicks;
    private Context context;
    int score;

    public Buttons(PostModel model, PostModelHolder holder, DBAdapter db, Context context, int clicks, int score) {
        this.model = model;
        this.holder = holder;
        this.db = db;
        this.context = context;
        this.clicks = clicks;
        this.score = score;
    }


    public PostModel Bcontrol(final String dir) {// "1" "-1"

        if (NewsActivity.LOGGIN) {
            if (dir.equals("1")) {
                clicks = model.getClickup() + 1;
            } else {
                clicks = model.getClickdown() + 1;
            }

            if (clicks == 2) {
                new ButtonsTask(model.getName()) {
                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if (aBoolean) {
                            model.setScore(score);
                            holder.score.setText(String.valueOf(model.getScore()));
                            db.updateScore(model);
                            if (dir.equals("1")) {
                                holder.up.setBackgroundColor(Color.TRANSPARENT);
                                model.setClickup(0);
                            } else {
                                holder.down.setBackgroundColor(Color.TRANSPARENT);
                                model.setClickdown(0);
                            }
                            Toast.makeText(context, "un-voting", Toast.LENGTH_SHORT).show();

                        } else {
                            if (!NewsActivity.ACTIVE_USER) {
                                Toast.makeText(context, "Unauthorized. Logout!", Toast.LENGTH_LONG).show();
                                ((Activity) context).finish();
                            }
                        }
                    }
                }.execute("0");


            } else {
                new ButtonsTask(model.getName()) {
                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        System.out.println(aBoolean);
                        if (aBoolean) {
                            if (dir.equals("1")) {
                                model.setClickdown(0);
                                model.setClickup(1);
                                holder.down.setBackgroundColor(Color.TRANSPARENT);
                                holder.up.setBackgroundColor(Color.DKGRAY);
                                model.setScore(score + 1);
                            } else {
                                model.setClickup(0);
                                model.setClickdown(1);
                                holder.up.setBackgroundColor(Color.TRANSPARENT);
                                holder.down.setBackgroundColor(Color.DKGRAY);
                                model.setScore(score - 1);
                            }
                            Toast.makeText(context, "voting", Toast.LENGTH_SHORT).show();
                            holder.score.setText(String.valueOf(model.getScore()));
                            db.updateScore(model);
                        } else {
                            if (!NewsActivity.ACTIVE_USER) {
                                Toast.makeText(context, "Unauthorized. Logout!", Toast.LENGTH_LONG).show();
                                ((Activity) context).finish();
                            }
                        }
                    }
                }.execute(dir);
            }

        } else {
            String url = String.format(NewsActivity.AUTH_URL, NewsActivity.CLIENT_ID, NewsActivity.STATE, NewsActivity.REDIRECT_URI);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }
        return model;
    }
}
