package org.vagabond.util.ewah;

/*
 * Copyright 2009-2012, Daniel Lemire, Cliff Moon, David McIntosh and Robert Becho
 * Licensed under APL 2.0.
 */

/**
 * Mostly for internal use.
 *
 * @since 0.1.0
 * @author Daniel Lemire
 */
public final class RunningLengthWord {

  /**
   * Instantiates a new running length word.
   *
   * @param a an array of 64-bit words
   * @param p position in the array where the running length word is located.
   */
  public RunningLengthWord(final long[] a, final int p) {
    this.array = a;
    this.position = p;
  }
  
  /**
   * Initializes the fields with values from other.
   * @param other
   */
  RunningLengthWord(RunningLengthWord other) {
	  this.set(other);
  }
  
  /**
   * Copies values from other.
   * @param other
   */
  public void set (RunningLengthWord other) {
	  this.array = other.array;
	  this.position = other.position;
  }
  
  /**
   * Gets the number of literal words.
   *
   * @return the number of literal words
   */
  public int getNumberOfLiteralWords() {
    return (int) (this.array[this.position] >>> (1 + runninglengthbits));
  }

  /**
   * Sets the number of literal words.
   *
   * @param number the new number of literal words
   */
  public void setNumberOfLiteralWords(final long number) {
    this.array[this.position] |= notrunninglengthplusrunningbit;
    this.array[this.position] &= (number << (runninglengthbits + 1))
      | runninglengthplusrunningbit;
  }

  /**
   * Sets the running bit.
   *
   * @param b the new running bit
   */
  public void setRunningBit(final boolean b) {
    if (b)
      this.array[this.position] |= 1l;
    else
      this.array[this.position] &= ~1l;
  }

  /**
   * Gets the running bit.
   *
   * @return the running bit
   */
  public boolean getRunningBit() {
    return (this.array[this.position] & 1) != 0;
  }

  /**
   * Gets the running length.
   *
   * @return the running length
   */
  public long getRunningLength() {
    return (this.array[this.position] >>> 1) & largestrunninglengthbits;
  }

  /**
   * Sets the running length.
   *
   * @param number the new running length
   */
  public void setRunningLength(final long number) {
    this.array[this.position] |= shiftedlargestrunninglengthcount;
    this.array[this.position] &= (number << 1)
      | notshiftedlargestrunninglengthcount;
  }

  /**
   * Return the size in uncompressed words represented by
   * this running length word.
   *
   * @return the long
   */
  public long size() {
    return getRunningLength() + getNumberOfLiteralWords();
  }

  /**
   * 
   */
  public RunningLengthWord next() {
	  position += getNumberOfLiteralWords() + 1;
	  return this;
  }
  
  public RunningLengthWord getNext() {
	  return new RunningLengthWord(this).next();
  }
  
  /* 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "running bit = " + getRunningBit() + " running length = "
      + getRunningLength() + " number of lit. words "
      + getNumberOfLiteralWords() + " at pos " + position;
  }

  @Override
  public boolean equals (Object other) {
	  if (other == null)
		  return false;
	  
	  if (other instanceof RunningLengthWord) {
		RunningLengthWord o = (RunningLengthWord) other;
		return o.position == this.position && o.array == this.array;
	  }
	  
	  return false;
  }
  
  
  /** The array of words. */
  public long[] array;
  
  /** The position in array. */
  public int position;
  
  /** number of bits dedicated to marking  of the running length of clean words */  
  public static final int runninglengthbits = 32;
  
  private static final int literalbits = 64 - 1 - runninglengthbits;
  
  /** largest number of dirty words in a run. */
  public static long largestliteralcount = (1l << literalbits) - 1;
  
  /** largest number of clean words in a run */
  public static long largestrunninglengthcount = (1l << runninglengthbits) - 1;
  
  /** bitmask for largest running length */
  public static final long largestrunninglengthbits = (1l << runninglengthbits) - 1;
  
  private static final long shiftedlargestrunninglengthcount = largestrunninglengthcount << 1;
  
  private static final long notshiftedlargestrunninglengthcount = ~shiftedlargestrunninglengthcount;
  
  private static final long runninglengthplusrunningbit = (1l << (runninglengthbits + 1)) - 1;
  
  private static final long notrunninglengthplusrunningbit = ~runninglengthplusrunningbit;
  
}