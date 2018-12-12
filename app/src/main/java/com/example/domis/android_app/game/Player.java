package com.example.domis.android_app.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import com.example.domis.android_app.R;

public class Player {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 0;
    private boolean boosting;
    private boolean left;
    private boolean right;
    private int maxX;
    private int minX;

    private final int MIN_SPEED = -50;
    private final int MAX_SPEED = 50;

    private Rect detectCollision;

    public Player(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencar); // assign an green car icon to the user
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false); // scale the user car
        x = screenX / 2; // car set in the middle according to the X-axis
        Log.e("X", x + "");
        y = screenY - (bitmap.getHeight() * 2); // car set positioned according to the Y-axis
        speed = 0;
        minX = 0;
        maxX =  screenX - bitmap.getWidth();
        //boosting = false;

        //initializing rect object
        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void setBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
        left = false;
        right = false;
    }

    public void update() {
            if(left)
            {
                speed -= 10; // set speed to 10 when moving left
            }
            else if(right)
            {
                speed += 10; // set speed to 10 when moving right
            }
            else
            {
                speed = 0; // stop in one position
            }
        Log.e("Speed: ", speed + "");

        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }

        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        //y -= speed + GRAVITY;
        x += speed;

        if (x < minX) {
            x = minX; // car has to stay on the screen
        }
        if (x > maxX) {
            x = maxX; // car has to stay on the screen
        }

        //adding top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }

    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public void moveLeft()
    {
        left = true;
    }

    public void moveRight()
    {
        right = true;
    }

}