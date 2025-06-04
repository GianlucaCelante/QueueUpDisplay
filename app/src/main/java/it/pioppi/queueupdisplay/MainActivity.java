package it.pioppi.queueupdisplay;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.pioppi.queueupdisplay.animation.SlideInUpAnimator;
import it.pioppi.queueupdisplay.business.HistoryAdapter;
import it.pioppi.queueupdisplay.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private DatabaseReference database;
    private HistoryAdapter adapter;
    private List<Integer> numbers;
    private String lastValue = null;
    private SoundPool soundPool;
    private int soundId;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        database = FirebaseDatabase.getInstance(Constants.DATABASE_URL).getReference();

        RecyclerView historyList = findViewById(R.id.historyNumber);
        LinearLayoutManager managerLayout = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        historyList.setLayoutManager(managerLayout);
        numbers = new ArrayList<>();
        adapter = new HistoryAdapter(this, numbers);
        historyList.setAdapter(adapter);

        SlideInUpAnimator animator = new SlideInUpAnimator();
        historyList.setItemAnimator(animator);

        soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
        soundId = soundPool.load(this, R.raw.next_number_sound, 1);
   }

    @Override
    protected void onResume() {
        super.onResume();

        database.child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value != null && !value.equals(lastValue)) {
                    adapter.notifyItemRangeRemoved(0, numbers.size());
                    numbers.add(0, Integer.parseInt(value));
                    adapter.notifyItemRangeInserted(0, numbers.size());
                    lastValue = value;
                    logger.info("Data has been changed, current stored number retrieved from database: {}", value);

                    handler.post(() -> soundPool.play(soundId, 1, 1, 0, 0, 1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                logger.error("Error while retrieving number from database: {}, {}", error.getMessage(), error.getDetails());
            }
        });
    }
}