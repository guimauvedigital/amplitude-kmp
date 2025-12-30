Pod::Spec.new do |spec|
    spec.name                     = 'kmp'
    spec.version                  = '1.0.2'
    spec.homepage                 = 'https://github.com/amplitude/amplitude-kmp'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Amplitude Analytics KMP SDK'
    spec.vendored_frameworks      = 'build/cocoapods/framework/AmplitudeKMP.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '13.0'
    spec.dependency 'AmplitudeCore'
    spec.dependency 'AmplitudeSwift', '~> 1.16.2'
    spec.dependency 'AnalyticsConnector', '~> 1.3.0'
    if !Dir.exist?('build/cocoapods/framework/AmplitudeKMP.framework') || Dir.empty?('build/cocoapods/framework/AmplitudeKMP.framework')
        raise "
        Kotlin framework 'AmplitudeKMP' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:
            ./gradlew :kmp:generateDummyFramework
        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end
    spec.xcconfig = {
        'ENABLE_USER_SCRIPT_SANDBOXING' => 'NO',
    }
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':kmp',
        'PRODUCT_MODULE_NAME' => 'AmplitudeKMP',
    }
    spec.script_phases = [
        {
            :name => 'Build kmp',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                    echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                    exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
end
