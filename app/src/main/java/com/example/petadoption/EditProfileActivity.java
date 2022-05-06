package com.example.petadoption;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class EditProfileActivity extends AppCompatActivity
{
    private Button leftBtn, middleLeftBtn, middleRightBtn, rightBtn,btnChangePfp;
    private ImageView pfp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        leftBtn = findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EditProfileActivity.this, EditProfileActivity.class));
            }
        });

        middleLeftBtn = findViewById(R.id.middleLeftBtn);
        middleLeftBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            }
        });

/*        middleRightBtn = findViewById(R.id.middleRightBtn);            all just placeholders for when we combine the rest of the pages
        middleRightBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class)); // edit when get post viewer scene
            }
        });

        rightBtn = findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class)); // edit when get create post scene
            }
        });

        btnChangePfp = findViewById(R.id.btnChangePfp);
        btnChangePfp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class)); // edit when get chat system
            }
        });*/

        //pfp = findViewById(R.id.pfp);

    }

    // placeholder for toolbar button functionality and profile
/*    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.leftBtn:
                Toast.makeText(this, "Left Button Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.middleLeftBtn:
                Toast.makeText(this, "Middle Left Button Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.middleRightBtn:
                Toast.makeText(this, "Middle Right Button Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rightBtn:
                Toast.makeText(this, "Right Button Pressed", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnChangePfp:
                // Open Galley
                // Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                Toast.makeText(this, "Profile button Pressed", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }*/
}