package com.fazemeright.library.api

interface Storable {
    /**
     * Get map of the object.
     *
     * @return map of data
     */
    val hashMap: Map<String, Any>

    /**
     * ID.
     *
     * @return id
     */
    val id: Long
}