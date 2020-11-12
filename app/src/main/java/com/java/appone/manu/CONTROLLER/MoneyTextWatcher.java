package com.java.appone.manu.CONTROLLER;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class MoneyTextWatcher implements TextWatcher {
    private final WeakReference<TextInputEditText> textInputEditTextWeakReference;
    private final Locale locale = new Locale("pt", "BR");

    public MoneyTextWatcher(TextInputEditText textInputEditText) {
        this.textInputEditTextWeakReference = new WeakReference<>(textInputEditText);
    }

    public static String formatPriceSave(String price) {
        String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());
        String cleanString = price.replaceAll(replaceable, "");
        StringBuilder stringBuilder = new StringBuilder(cleanString.replaceAll(" ", ""));

        return String.valueOf(stringBuilder.insert(cleanString.length() - 2, '.'));
    }

    private static String getCurrencySymbol() {
        return Objects.requireNonNull(NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency()).getSymbol();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        TextInputEditText value = textInputEditTextWeakReference.get();
        value.removeTextChangedListener(this);

        BigDecimal bigDecimal = parseToBigDecimal(editable.toString());
        String formatted = NumberFormat.getCurrencyInstance(locale).format(bigDecimal);
        String replaceable = String.format("[%s\\s]", getCurrencySymbol());
        String cleanString = formatted.replaceAll(replaceable, "");

        value.setText(cleanString);
        value.setSelection(cleanString.length());
        value.addTextChangedListener(this);
    }

    private BigDecimal parseToBigDecimal(String value) {
        String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());

        String cleanString = value.replaceAll(replaceable, "");

        try {
            return new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        } catch (NumberFormatException e) {
            return new BigDecimal(0);
        }
    }
}
