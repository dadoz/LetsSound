package com.application.letssound.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by davide on 12/08/2017.
 */

public class LatestPlayItemTouchHelper extends ItemTouchHelper {
    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     *  Callback which controls the behavior of this touch helper.
     */
    public LatestPlayItemTouchHelper(ItemTouchSimpleCallbacks callbacks, RecyclerView.Adapter adapter) {
        super(callbacks);
        callbacks.setAdapter(adapter);
    }

    public static class ItemTouchSimpleCallbacks extends ItemTouchHelper.SimpleCallback {

        private final int swipeDirs;
        private RecyclerView.Adapter adapter;

        /**
         * Creates a Callback for the given drag and swipe allowance. These values serve as
         * defaults
         * and if you want to customize behavior per ViewHolder, you can override
         * {@link #(RecyclerView, )}
         * and / or {@link #(RecyclerView, )}.
         *
         * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
         *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
         *                  #END},
         *                  {@link #UP} and {@link #DOWN}.
         * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
         *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
         *                  #END},
         *                  {@link #UP} and {@link #DOWN}.
         */
        public ItemTouchSimpleCallbacks(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
            this.swipeDirs = swipeDirs;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (adapter instanceof ItemTouchHelperAdapter)
                ((ItemTouchHelperAdapter) adapter).onItemDismiss(viewHolder.getAdapterPosition());
        }

        public void setAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }
    }

}
