import no.elhub.devxp.build.configuration.pipeline.constants.Group
import no.elhub.devxp.build.configuration.pipeline.constants.KubeCluster
import no.elhub.devxp.build.configuration.pipeline.dsl.elhubProject
import no.elhub.devxp.build.configuration.pipeline.extensions.triggerOnVcsChange
import no.elhub.devxp.build.configuration.pipeline.jobs.gitOps
import no.elhub.devxp.build.configuration.pipeline.jobs.gradleJib
import no.elhub.devxp.build.configuration.pipeline.jobs.gradleVerify

// Must be one of https://github.com/elhub/plat-cirrus/blob/c074020e7227102fff5b7bc7f95ccef2386b0f75/terraform/workspace_vars/prod.yaml#L30
val imageRepo = "GROUP/REPONAME"
val gitOpsRepo = "https://github.com/elhub/GROUP"

elhubProject(group = Group.DEVXP, name = "actor-registry-api") {
    pipeline {
        sequential {
            gradleVerify()

//            gradleJib {
//                registrySettings = {
//                    repository = imageRepo
//                }
//            }
//
//            gitOps {
//                cluster = KubeCluster.YOUR_CLUSTER
//                gitOpsRepository = gitOpsRepo
//            }.triggerOnVcsChange()
        }
    }
}
