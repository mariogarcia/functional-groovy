package groovyfp.oop2fn

class VideoBuilder {

    String name
    String type
    Long length

    static VideoBuilder builder() {
        return new VideoBuilder()
    }

    VideoBuilder name(String name) {
        this.name = name
        return this
    }

    VideoBuilder type(String type) {
        this.name = name
        return this
    }

    VideoBuilder length(Long length) {
        this.length = length
        return this
    }

    Video build() {
        return new Video(name, type, length)
    }

}
