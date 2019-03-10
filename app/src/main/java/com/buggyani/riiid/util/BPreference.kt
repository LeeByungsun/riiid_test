package com.buggyani.test.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by bslee on 2019-03-10.
 */
class BPreference private constructor(private var mContext: Context) {
    private val mPref: SharedPreferences
    private val mEditor: SharedPreferences.Editor?

    init {
        mPref = mContext.getSharedPreferences("WALLET_TEST", Context.MODE_PRIVATE)
        mEditor = mPref.edit()
    }

    /**
     * Stores String value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String, value: String) {
        mEditor!!.putString(key, value)
        mEditor.commit()
    }

    /**
     * Stores int value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String, value: Int) {
        mEditor!!.putInt(key, value)
        mEditor.commit()
    }

    /**
     * Stores Double value in String format in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String, value: Double) {
        setValue(key, java.lang.Double.toString(value))
    }

    /**
     * Stores long value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String, value: Long) {
        mEditor!!.putLong(key, value)
        mEditor.commit()
    }

    /**
     * Stores boolean value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String, value: Boolean) {
        mEditor!!.putBoolean(key, value)
        mEditor.commit()
    }

    /**
     * Retrieves String value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    fun getStringValue(key: String, defaultValue: String?): String? {
        return mPref.getString(key, defaultValue)
    }

    /**
     * Retrieves int value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    fun getIntValue(key: String, defaultValue: Int): Int {
        return mPref.getInt(key, defaultValue)
    }

    /**
     * Retrieves long value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    fun getLongValue(key: String, defaultValue: Long): Long {
        return mPref.getLong(key, defaultValue)
    }

    /**
     * Retrieves boolean value from preference
     *
     * @param keyFlag      key of preference
     * @param defaultValue default value if no key found
     */
    fun getBooleanValue(keyFlag: String, defaultValue: Boolean): Boolean {
        return mPref.getBoolean(keyFlag, defaultValue)
    }

    /**
     * Removes key from preference
     *
     * @param key key of preference that is to be deleted
     */
    fun removeKey(key: String) {
        if (mEditor != null) {
            mEditor.remove(key)
            mEditor.commit()
        }
    }

    /**
     * Clears all the preferences stored
     */
    fun clear() {
        mEditor!!.clear().commit()
    }

    companion object {
        private var instance: BPreference? = null


        fun getInstance(context: Context): BPreference {
            println("create instance")
            if (instance == null) {
                instance = BPreference(context.applicationContext)
            }
            return instance!!
        }
    }

}

