# solr-libstdnum-normalize : Normalize ISBN/LCCN

**DEPRECATED**. Use [https://github.com/billdueber/umich_solr_library_filters](https://github.com/billdueber/umich_solr_library_filters) instead.

The package contains two solr text filters for people who use Solr and want to more intelligently deal with library data types (currently, just ISBNs and LCCNs). 

* `edu.umich.lib.solr.analysis.LCCNNormalizerFilterFactory` will attempt to normalize a token as an LCCN, as specified in the document at http://www.loc.gov/marc/lccn-namespace.html. 

* `edu.umich.lib.solr.analysis.ISBNLongifierFilterFactory` will take in a string that contains (hopefully) and ISBN, find it, and return the equivalent ISBN-13. ISBNs that are already 13 digits will be cleaned up (remove dashes, etc.) and passed through. Anything that can't be identified as probably an ISBN is thrown away (resulting in no tokens).

Neither of these filters is particularly smart, and normalization is done regardless of whether, for example, the check-digits are correct for ISBNs (since actual data tends to be too messy for this to be workable).

## Why use this?

Many library standard numbers have very flexible syntax (e.g., ISBNs can have dashes pretty much anywhere, the trailing 'X' can be upper- or lower-case, etc.). Instead of trying to deal with all this consistently in both your indexing and solr-query-generating code, stick it into an analyzer and let Solr do the exact same thing on both query and index so you're sure they'll match.


## Fair warning

**Output of the LCCN filter on arbitrary input is undefined!** The LCCN syntax is fluid enough that it's impossible to do a simple syntax check. Therefore, only send this field type things you're pretty sure are LCCNs (e.g., the contens of a MARC 010). Other stuff gets munged, but not in a useful way.

**Output of the ISBN filter on non-ISBNs is the empty string!** Anything that cannot syntactically be an ISBN is thrown away; if that leaves nothing, you end up with nothing (so you can't use it to serach for arbitrary text that might be alongside/instead of an ISBN). Again, use this field type only for things you're pretty sure actually contains an ISBN (usually in the MARC 020).

## How to  use the field types

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
 ig    <tokenizer class="solr.PatternTokenizerFactory" pattern="[;,]\s*" />         
     <filter class="edu.umich.lib.solr.analysis.LCCNNormalizerFilterFactory"/> 
   </analyzer>
</fieldType>

```

3. Define fields that use the new types. Something like:

```xml
<field name="isbn" type="ISBNLong"       indexed="true" multiValued="true" />
<field name="lccn" type="LCCNNormalized" indexed="true" multiValued="true" />
```

## Building the jar file

If for whatever reason you want to integrate this into your own Solr build process, it's easy to do so (thanks to Jay Luker, Jonathan Rochkind, and Adam Constabaris for the work to make this happen). 

* Clone the repo into `solr/contrib`
* Run `ant jar`

You'll end up with a jar file of the form `solr/build/contrib/solr-libstdnum-normalize/solr-libstdnum-normalize-4.3-SNAPSHOT.jar`


## Note on Patches/Pull Requests

* Fork the project.
* Make your feature addition or bug fix.
* Add tests for it. This is important so I don't break it in a
  future version unintentionally.
* Commit, do not mess with rakefile, version, or history.
  (if you want to have your own version, that is fine but bump version in a commit by itself I can ignore when I pull)
* Send me a pull request. Bonus points for topic branches.

## Copyright

Copyright (c) 2009-2013, The Regents of the University of Michigan. See LICENSE for details.

