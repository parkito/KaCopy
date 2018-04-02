<h3 align="center">
  <br>
   <img src="https://s8.hostingkartinok.com/uploads/images/2018/03/68b491a97174353014a1f92aaecba400.png" alt="Logo ArrayMixer" title="KaCopy logo" />
  <br>
 <strong>KaCopy</strong> is the compact high effective library for deep cloning java objects 
</h3>
<p align="center">  
<a href="https://www.codacy.com/app/parkito/KaCopy?utm_source=github.com&utm_medium=referral&utm_content=parkito/KaCopy&utm_campaign=badger"><img src="https://api.codacy.com/project/badge/Grade/f57835866b44434eb3676675f86c7b76"></a>
<a href="https://www.codacy.com/app/parkito/KaCopy?utm_source=github.com&utm_medium=referral&utm_content=parkito/KaCopy&utm_campaign=Badge_Coverage"><img src="https://api.codacy.com/project/badge/Coverage/8a941e0f57c047c8a481f4854666b42d"></a>
<a href="https://travis-ci.org/parkito/KaCopy"><img src="https://travis-ci.org/teles/array-mixer.svg?branch=master"></a>
 <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/license-MIT-blue.svg"></a>
</p>
  
  KaCopy uses: 
  * concepts of [Deep cloning library](https://github.com/kostaskougios/cloning) and 
  [Objenesis library](https://github.com/easymock/objenesis) for user friendly usage 
  * platform specific reflection API for  high effectiveness. 

## Table of contents

  * [Theory](#theory) 
  * [Common usage](#common-usage)
  * [Installation](#installation)
     * [Gradle](#gradle)
     * [Maven](#maven)
  * [Api](#api)
  * [Versions](#versions)
     * [Version table](#aliases)
  * [Examples](#examples)
  * [License](#license)

## Theory
Cloning means creating a new object from an already presented object and
copying all data of object to that new object.

Deep copy is creating a new object and then copying the non-static
fields of the current object to the new object. If a field
is a value type, a bit by bit copy of the field is performed.
If a field is a reference type, a new copy of the referred
object is performed. A deep copy of an object is a new object with
entirely new instance variables, it does not share objects with the old.

## Common usage

**ru.siksmfp.kacopy.api.KaCopier** is the main api class of KaCopy.
So, you need just create instance of KaCopier and use all functionality of framework

```java
KaCopier copier = new KaCopier(); 
```
KaCopy has next public elements

* **CopierSettings** is the property class for storing additional settings.
You could use it if extra settings is needed.
For example next instruction allows clone anonymous parent of class.
```java
copier.settings.cloneAnonymousParent(true); 
```
* **T deepCopy(T object)** is the method for deep object's cloning. It returns new instance of
 object with the same content as original has. 
 
 * **T shallowCopy(T object)** is the method for shallow copying.
  A new object is created that has an exact copy of the values in the original object. If any of the fields of the object are references to other objects, just the reference addresses are copied i.e., only the memory address is copied

## Installation

### Maven

```bash
<dependency>
    <groupId>ru.siksmfp</groupId>
    <artifactId>kacopy</artifactId>
    <version>0.0.8</version>
</dependency>
```

### Gradle

```bash
compile 'ru.siksmfp:kacopy:0.0.8'
```

## API

API consists of next elements

**CopierSettings** is the class contains optional setting for KaCopy class

**IDeepCloner** is the interface describes deep cloners - instrument for overriding cloning of adjusted classed. So, you can implement your clone-logic for specific classes.

**IFastCloner** is the interface for fast cloners overriding. You can implement specific logic for fast cloning specific classes 

**Immutable** is a annotation for marking class which doesn't have needs for copying 

**KaCopier** is the main class for full functional providing


## Versions

All major versions (x.\*.* ) are backward compatible. Minor versions (*.x.y) contain only bug fixing and no api changes

## Examples
```java
List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> integerListClone = kaCopier.deepCopy(integerList);
```

`integerListClone` is the full clone of `integerList`

## License

[MIT](https://github.com/parkito/KaCopy/blob/master/LICENSE)
