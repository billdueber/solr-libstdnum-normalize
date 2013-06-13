# What is this?

The package contains two solr text filters

* `edu.umich.lib.solr.analysis.LCCNNormalizerFilterFactory` will attempt to normalize a token as an LCCN, as specified in the document at http://www.loc.gov/marc/lccn-namespace.html. 

* `edu.umich.lib.solr.analysis.ISBNLongifierFilterFactory` will take in a string that contains (hopefully) and ISBN, find it, and return the equivalent ISBN-13. ISBNs that are already 13 digits will be cleaned up (remove dashes, etc.) and passed through. Anything that can't be identified as probably an ISBN is thrown away (resulting in no tokens).

Neither of these filters is particularly smart, and normalization is done regardless of whether, for example, the check-digits are correct for ISBNs (since actual data tends to be too messy for this to be workable).

**Output of the LCCN filter on arbitrary input is undefined!** The LCCN syntax is fluid enough that it's impossible to do a simple syntax check. Therefore, only send this field type things you're pretty sure are LCCNs (e.g., the contens of a MARC 010).

**Output of the ISBN filter on non-ISBNs is the empty string!** Anything that cannot syntactically be an ISBN is thrown away; if that leaves nothing, you end up with nothing (so you can't use it to serach for arbitrary text that might be alongside/instead of an ISBN). Again, use this field type only for things you're pretty sure actually contains an ISBN (usually in the MARC 020).

# How to  use the field types

1. Grab a jar file out of the `jarfiles` directory and put it somewhere solr will look for it (often, this can be $SOLR_HOME/lib).
2. Add something like the following to your `schema.xml` in the `<types>` section.

```xml

<!-- 
   Special library types. 
   ISBNs will (on index and query) be normalized and turned into ISBN13s.
   LCCNs will be normalized.
   
   In both cases, we split on comma- or semi-colon delimited lists,
   so you can throw a few in there at a time.
   
-->
<fieldType name="ISBNLong" class="solr.TextField"  omitNorms="true">
   <analyzer>
     <tokenizer class="solr.PatternTokenizerFactory" pattern="[;,]\s*" />         
     <filter class="edu.umich.lib.solr.analysis.ISBNLongifierFilterFactory"/> 
   </analyzer>
 </fieldType>
 

<fieldType name="LCCNNormalized" class="solr.TextField"  omitNorms="true">
   <analyzer>
     <tokenizer class="solr.PatternTokenizerFactory" pattern="[;,]\s*" />         
     <filter class="edu.umich.lib.solr.analysis.LCCNNormalizerFilterFactory"/> 
   </analyzer>
</fieldType>

```

3. Define fields that use the new types. Something like:

```xml
<field name="isbn" type="ISBNLong"       indexed="true" multiValued="true" />
<field name="lccn" type="LCCNNormalized" indexed="true" multiValued="true" />
```

# Building the jar file