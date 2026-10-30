package com.example.agriecommerce.presentation.viewmodel;

import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private int scrollPosition = 0;

    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int position) {
        this.scrollPosition = position;
    }
}
