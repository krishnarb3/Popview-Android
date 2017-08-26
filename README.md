# Popview-Android
Pop animation with circular dust effect for any view updation

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Popview--Android-green.svg?style=true)](https://android-arsenal.com/details/1/3813)

![Screenshots](https://media.giphy.com/media/FoGScttOF8e40/giphy.gif) 

# Getting Started 
<h4>In your build.gradle</h4>

```groovy
dependencies {
    compile 'rb.popview:popview:0.1.0'
}
```    
<h4>Usage</h4>

Initialize PopField like so :

```java
PopField popField = PopField.attach2window(activity);
```

<h5>Popping the view without replacement (1st icon)</h5>

```java
popField.popView(view);
```

<h5>Popping the view and replacing with new view without animation (2nd icon)</h5>

```java
popField.popView(view,newView);
```
Eg:

   ```java
   LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()        
    					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   final View addView = layoutInflater.inflate(R.layout.sampleview, null);         //Inflate new view from xml
   TextView newTextView = (TextView) addView.findViewById(R.id.sampletextview);    //Reference the newview     
   newTextView.setText("New Sample text");
   popField.popView(view,addView);
   ```
   
<h5>Popping the view and replacing with new view with animation (3rd icon)</h5>

```java
popField.popView(view,newView,true);
```
Eg:

   ```java
   LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()        
    					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   final View addView = layoutInflater.inflate(R.layout.sampleview, null);         //Inflate new view from xml
   TextView newTextView = (TextView) addView.findViewById(R.id.sampletextview);    //Reference the newview     
   newTextView.setText("New Sample text");
   popField.popView(view,addView,true);
   ```

Inspired by and thanks to [Tyrantgit's Explosion field](https://github.com/tyrantgit/ExplosionField)

# License

Copyright 2017 krishnarb3

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
