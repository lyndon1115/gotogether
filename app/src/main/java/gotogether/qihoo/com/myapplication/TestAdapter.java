package gotogether.qihoo.com.myapplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class TestAdapter<T> extends BaseAdapter implements Filterable {

    /**
     * Contains the list of objects that represent the data of this
     * ArrayAdapter. The content of this list is referred to as "the array" in
     * the documentation.
     */
    private List<T> mObjects;
    /**
     *
     */
    private ArrayList<T> mObjectsPinyin;
    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is
     * also used by the filter (see {@link #getFilter()} to make a synchronized
     * copy of the original array of data.
     */
    private final Object mLock = new Object();

    /**
     * The resource indicating what views to inflate to display the content of
     * this array adapter.
     */
    private int mResource;

    /**
     * The resource indicating what views to inflate to display the content of
     * this array adapter in a drop down widget.
     */
    private int mDropDownResource;

    /**
     * If the inflated resource is not a TextView, {@link #mFieldId} is used to
     * find a TextView inside the inflated views hierarchy. This field must
     * contain the identifier that matches the one defined in the resource file.
     */
    private int mFieldId = 0;

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called
     * whenever {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange = true;

    private Context mContext;

    private ArrayList<T> mOriginalValues;
    /**
     *
     */
    private ArrayList<T> mOriginalValuesPinYin;
    private ArrayFilter mFilter;

    private LayoutInflater mInflater;

    /**
     * Constructor
     *
     * @param context
     *            The current context.
     * @param textViewResourceId
     *            The resource ID for a layout file containing a TextView to use
     *            when instantiating views.
     */
    public TestAdapter(Context context, int textViewResourceId) {
        init(context, textViewResourceId, 0, new ArrayList<T>());
    }

    /**
     * Constructor
     *
     * @param context
     *            The current context.
     * @param resource
     *            The resource ID for a layout file containing a layout to use
     *            when instantiating views.
     * @param textViewResourceId
     *            The id of the TextView within the layout resource to be
     *            populated
     */
    public TestAdapter(Context context, int resource, int textViewResourceId) {
        init(context, resource, textViewResourceId, new ArrayList<T>());
    }

    /**
     * Constructor
     *
     * @param context
     *            The current context.
     * @param textViewResourceId
     *            The resource ID for a layout file containing a TextView to use
     *            when instantiating views.
     * @param objects
     *            The objects to represent in the ListView.
     */
    public TestAdapter(Context context, int textViewResourceId, T[] objects) {
        init(context, textViewResourceId, 0, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context
     *            The current context.
     * @param resource
     *            The resource ID for a layout file containing a layout to use
     *            when instantiating views.
     * @param textViewResourceId
     *            The id of the TextView within the layout resource to be
     *            populated
     * @param objects
     *            The objects to represent in the ListView.
     */
    public TestAdapter(Context context, int resource, int textViewResourceId,
                       T[] objects) {
        init(context, resource, textViewResourceId, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context
     *            The current context.
     * @param textViewResourceId
     *            The resource ID for a layout file containing a TextView to use
     *            when instantiating views.
     * @param objects
     *            The objects to represent in the ListView.
     */
    public TestAdapter(Context context, int textViewResourceId, List<T> objects) {
        init(context, textViewResourceId, 0, objects);
    }

    /**
     * Constructor
     *
     * @param context
     *            The current context.
     * @param resource
     *            The resource ID for a layout file containing a layout to use
     *            when instantiating views.
     * @param textViewResourceId
     *            The id of the TextView within the layout resource to be
     *            populated
     * @param objects
     *            The objects to represent in the ListView.
     */
    public TestAdapter(Context context, int resource, int textViewResourceId,
                       List<T> objects) {
        init(context, resource, textViewResourceId, objects);
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object
     *            The object to add at the end of the array.
     */
    public void add(T object) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.add(object);
                if (mNotifyOnChange)
                    notifyDataSetChanged();
            }
        } else {
            mObjects.add(object);
            if (mNotifyOnChange)
                notifyDataSetChanged();
        }
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object
     *            The object to insert into the array.
     * @param index
     *            The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.add(index, object);
                if (mNotifyOnChange)
                    notifyDataSetChanged();
            }
        } else {
            mObjects.add(index, object);
            if (mNotifyOnChange)
                notifyDataSetChanged();
        }
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object
     *            The object to remove.
     */
    public void remove(T object) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.remove(object);
            }
        } else {
            mObjects.remove(object);
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.clear();
            }
        } else {
            mObjects.clear();
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator
     *            The comparator used to sort the objects contained in this
     *            adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        Collections.sort(mObjects, comparator);
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}. If set to false, caller must manually call
     * notifyDataSetChanged() to have the changes reflected in the attached
     * view.
     *
     * The default is true, and calling notifyDataSetChanged() resets the flag
     * to true.
     *
     * @param notifyOnChange
     *            if true, modifications to the list will automatically call
     *            {@link #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    private void init(Context context, int resource, int textViewResourceId,
                      List<T> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mObjects = objects;
        // -------------
        mObjectsPinyin = new ArrayList<T>();
        for (T t : objects) {
            mObjectsPinyin.add((T) getPinYin(t.toString()));//
        }
        // ------------
        mFieldId = textViewResourceId;
    }

    /**
     * Returns the context associated with this array adapter. The context is
     * used to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return mObjects.size();
    }

    /**
     * {@inheritDoc}
     */
    public T getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item
     *            The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView,
                                        ViewGroup parent, int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                // If no custom field is assigned, assume the whole resource is
                // a TextView
                text = (TextView) view;
            } else {
                // Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter",
                    "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        T item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence) item);
        } else {
            text.setText(item.toString());
        }

        return view;
    }

    /**
     * <p>
     * Sets the layout resource to create the drop down views.
     * </p>
     *
     * @param resource
     *            the layout resource defining the drop down views
     * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
     */
    public void setDropDownViewResource(int resource) {
        this.mDropDownResource = resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent,
                mDropDownResource);
    }

    /**
     * Creates a new ArrayAdapter from external resources. The content of the
     * array is obtained through
     * {@link android.content.res.Resources#getTextArray(int)}.
     *
     * @param context
     *            The application's environment.
     * @param textArrayResId
     *            The identifier of the array to use as the data source.
     * @param textViewResId
     *            The identifier of the layout used to create views.
     *
     * @return An ArrayAdapter<CharSequence>.
     */
    public static ArrayAdapter<CharSequence> createFromResource(
            Context context, int textArrayResId, int textViewResId) {
        CharSequence[] strings = context.getResources().getTextArray(
                textArrayResId);
        return new ArrayAdapter<CharSequence>(context, textViewResId, strings);
    }

    /**
     * {@inheritDoc}
     */
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    /**
     *
     *
     * @param str
     * @return
     */
    public boolean isAllChinese(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ss = str.charAt(i);
            boolean s = String.valueOf(ss).matches("[\u4e00-\u9fa5]");
            if (!s) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     *
     * @param firstLetters
     *
     * @param str
     *
     * @return
     */
    public boolean isFirstLetterMatch(String firstLetters, String str) {
        String[] names = str.split(",");//
        if (firstLetters.length() > names.length) {
            return false;
        }
        for (int i = 0; i < firstLetters.length(); i++) {
            if (!names[i].startsWith(String.valueOf(firstLetters.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     *
     * @param str
     * @return
     */
    public boolean isAllPinYin(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ss = str.charAt(i);
            boolean s = String.valueOf(ss).matches("^[a-zA-Z]*");
            if (!s) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     *
     * @param inputString
     *
     * @return
     */
    public String getPinYin(String inputString) {

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        //
        // char[] input = inputString.trim().toCharArray();
        StringBuffer output = new StringBuffer("");

        try {
            for (int i = 0; i < inputString.length(); i++) {
                char ss = inputString.charAt(i);
                boolean isHanzi = String.valueOf(ss).matches("[\u4e00-\u9fa5]");
                if (isHanzi) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(ss,
                            format);
                    output.append(temp[0]);
                    output.append(",");
                } else
                    output.append(Character.toString(ss));
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        System.out.println("getPinYin" + output.toString());
        return output.toString();
    }

    /**
     * <p>
     * An array filter constrains the content of the array adapter with a
     * prefix. Each item that does not start with the supplied prefix is removed
     * from the list.
     * </p>
     */
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            System.out.println("performFiltering??" + prefix);
            boolean isChinese = isAllChinese(prefix.toString());
            boolean isAllPinyin = isAllPinYin(prefix.toString());

            // getPinYin(prefix.toString());
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<T>(mObjects);
                }
            }
            if (mOriginalValuesPinYin == null) {//
                synchronized (mLock) {
                    mOriginalValuesPinYin = new ArrayList<T>(mObjectsPinyin);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<T> list = new ArrayList<T>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final ArrayList<T> values = mOriginalValues;
                final ArrayList<T> valuesPinYin = mOriginalValuesPinYin;//
                final int count = values.size();

                final ArrayList<T> newValues = new ArrayList<T>(count);

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final T valuePinYIn = valuesPinYin.get(i);
                    final String valueText = value.toString().toLowerCase();
                    final String valueTextPinYin = valuePinYIn.toString()
                            .toLowerCase();
                    // First match against the whole, non-splitted value
                    if (isAllPinyin
                            && isFirstLetterMatch(prefixString, valueTextPinYin)) {//
                    } else if (isAllPinyin
                            && valueTextPinYin.startsWith(prefixString)) {//
                        newValues.add(value);
                    } else if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else if (!isAllPinyin) {

                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // noinspection unchecked
            mObjects = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}