/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordstack;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

public class LetterTile extends TextView {

    public static final int TILE_SIZE = 150;
    private Character letter;
    private boolean frozen;

    public LetterTile(Context context, Character letter) {
        super(context);
        this.letter = letter;
        setText(letter.toString());
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setHeight(TILE_SIZE);
        setWidth(TILE_SIZE);
        setTextSize(30);
        setBackgroundColor(Color.rgb(255, 255, 200));
    }

    public void moveToViewGroup(ViewGroup targetView) {
        ViewParent parent = getParent();
        if (parent instanceof StackedLayout ) {
            StackedLayout owner = (StackedLayout) parent;
            owner.pop();
            targetView.addView(this);
            freeze();
            setVisibility(View.VISIBLE);
        } else {
            ViewGroup owner = (ViewGroup) parent;
            owner.removeView(this);
            ((StackedLayout) targetView).push(this);
            unfreeze();
        }
    }

    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if(!this.frozen && motionEvent.getAction()==MotionEvent.ACTION_DOWN)
        {
            Log.e("mytag","called!");
            return startDrag(ClipData.newPlainText("",""),new View.DragShadowBuilder(this)
                    , this,0);

        }

        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        return super.onTouchEvent(motionEvent);
    }
}
