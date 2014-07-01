package groovyfp.oop2fn

import groovy.transform.TupleConstructor

@TupleConstructor
class Video {
    String name
    String type
    long length

    void setName(String name) {}
    void setType(String type) {}
    void setLength(Long length) {}
}
