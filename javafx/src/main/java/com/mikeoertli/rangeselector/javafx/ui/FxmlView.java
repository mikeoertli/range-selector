package com.mikeoertli.rangeselector.javafx.ui;

import com.mikeoertli.rangeselector.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public enum FxmlView
{
    MAIN_MENU(Constants.VIEW_DEMO_MAIN_TITLE_KEY, Constants.VIEW_DEMO_MAIN_MENU_FXML_KEY),
    DEMO_HISTOGRAM_LARGE(Constants.DEMO_HISTOGRAM_LARGE_TITLE, Constants.VIEW_DEMO_HISTOGRAM_LARGE_FXML_KEY),
    DEMO_HISTOGRAM_SMALL(Constants.DEMO_HISTOGRAM_SMALL_TITLE, Constants.VIEW_DEMO_HISTOGRAM_SMALL_FXML_KEY),
    DEMO_SIMPLE(Constants.DEMO_SIMPLE_TITLE, Constants.VIEW_DEMO_SIMPLE_FXML_KEY);

    private final String bundlePropertyTitleKey;
    private final String bundlePropertyFxmlKey;

    FxmlView(String bundlePropertyTitleKey, String bundlePropertyFxmlKey)
    {
        this.bundlePropertyTitleKey = bundlePropertyTitleKey;
        this.bundlePropertyFxmlKey = bundlePropertyFxmlKey;
    }

    public String getBundlePropertyTitleKey()
    {
        return bundlePropertyTitleKey;
    }

    public String getBundlePropertyFxmlKey()
    {
        return bundlePropertyFxmlKey;
    }

    public String getTitle()
    {
        return ResourceBundle.getBundle(Constants.BUNDLE_FILE_NAME).getString(getBundlePropertyTitleKey());
    }

    public String getFxmlResourcePath()
    {
        return ResourceBundle.getBundle(Constants.BUNDLE_FILE_NAME).getString(getBundlePropertyFxmlKey());
    }

    public URL getFxmlResourceUrl(Class<?> referenceClass)
    {
        return Objects.requireNonNull(referenceClass.getResource(getFxmlResourcePath()));
    }

    public Parent getAndLoadFxmlResource(Class<?> referenceClass) throws IOException
    {
        return FXMLLoader.load(getFxmlResourceUrl(referenceClass));
    }
}
