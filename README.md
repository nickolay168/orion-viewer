Orion Viewer is pdf, djvu, xps, cbz and tiff file viewer for Android
devices based on
[mupdf](https://mupdf.com) and
[DjVuLibre](https://sourceforge.net/p/djvu/djvulibre-git/ci/master/tree/)
libraries

### Application features
* Outline navigation
* Bookmarks support
* Page navigation by screen taps + Tap Zones + Key binding
* Text selection
* Single word selection by double tap with translation in external dictionary
* Custom zoom
* Custom border crop
* Portrait/landscape orientation
* Support different navigation patterns inside page (left to right, right to left)
* External Dictionaries support
* Built-in file manager with recently opened file view

<a href='https://play.google.com/store/apps/details?id=universe.constellation.orion.viewer&hl=en&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

## How to build project?

To build `Orion Viewer` you will need:

 * Android Studio 3.6.2
 * android-sdk 28+
 * android-ndk 20+
 * make and python2 for mupdf
 * git

 * downloaded native libs (mupdf, djvu):

    `./gradlew -b  thirdparty_build.gradle downloadAndPatchDjvu downloadAndMakeMupdf`

    Build scripts for them are defined in 'externalNativeBuild' in gradle build files
    (for details see 'djvuModule/build.gradle' and 'mupdfModule/build.gradle').
    Native libs are checked out into 'nativeLibs/djvu' and 'nativeLibs/mupdf' folders.

 * specify path to android-sdk in 'local.properties' (use 'local.properties.sample' as example).

 * By default sources for native libs are included to build.
  If you have any freezes with IDE you can exclude them via 'orion.excludeNativeLibsSources'
  flag in local.properties.
